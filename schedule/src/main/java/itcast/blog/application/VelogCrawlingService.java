package itcast.blog.application;

import itcast.ai.application.GPTService;
import itcast.ai.dto.request.GPTSummaryRequest;
import itcast.ai.dto.request.Message;
import itcast.blog.client.VelogHttpClient;
import itcast.blog.parser.VelogDataParser;
import itcast.blog.repository.BlogRepository;
import itcast.domain.blog.Blog;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class VelogCrawlingService {

    private final VelogHttpClient velogHttpClient;
    private final VelogDataParser velogDataParser;
    private final BlogRepository blogRepository;
    private final GPTService gptService;

    public void crawlBlogs() {
        String query = """
                query trendingPosts($input: TrendingPostsInput!) {
                    trendingPosts(input: $input) {
                        title
                        user {
                            username
                        }
                        url_slug
                    }
                }
                """;

        String variables = """
                {
                    "input": {
                        "limit": 20,
                        "offset": 40,
                        "timeframe": "day"
                    }
                }
                """;

        String jsonResponse = velogHttpClient.fetchTrendingPostsOfJson(query, variables);
        List<String> blogUrls = velogDataParser.getBlogUrls(jsonResponse);

        List<String> existingUrls = blogRepository.findAllLinks();
        List<String> filteredBlogUrls = blogUrls.stream()
                .filter(blogUrl -> !existingUrls.contains(blogUrl))
                .toList();
        List<Blog> blogs = velogDataParser.parseTrendingPosts(filteredBlogUrls);

        blogs.forEach(blog -> {
                    Blog originalBlog = Blog.createVelogBlog(blog.getTitle(), blog.getOriginalContent(), blog.getPublishedAt(),
                            blog.getLink(), blog.getThumbnail());
                    Blog savedId = blogRepository.save(originalBlog);
                    Message message = new Message("user", blog.getOriginalContent());
                    GPTSummaryRequest request = new GPTSummaryRequest("gpt-4o-mini", message, 0.7f);
                    gptService.updateBlogBySummaryContent(request, savedId.getId());
                }
        );
    }

    @Scheduled(cron = "${scheduler.cron.crawling}")
    public void velogCrawling() {
        log.info("Velog Crawling Start ...");
        crawlBlogs();
        log.info("Velog Crawling & Save!");
    }
}
