package itcast.blog.application;

import itcast.blog.parser.VelogDataParser;
import itcast.blog.client.VelogHttpClient;
import itcast.blog.repository.BlogRepository;
import itcast.domain.blog.Blog;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class VelogCrawlingService {

    private final VelogHttpClient velogHttpClient;
    private final VelogDataParser velogDataParser;
    private final BlogRepository blogRepository;

    public List<Blog> crawlBlogs() {
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
        return blogs;
    }

    @Scheduled(cron = "${scheduler.cron.crawling}")
    public void velogCrawling() {
        log.info("Velog Crawling Start ...");

        List<Blog> blogs = crawlBlogs();
        blogRepository.saveAll(blogs);

        log.info("Velog Crawling & Save!");
    }
}
