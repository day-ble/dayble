package itcast.blog.application;

import itcast.ai.application.GPTService;
import itcast.ai.dto.request.GPTSummaryRequest;
import itcast.ai.dto.request.Message;
import itcast.blog.client.VelogHttpClient;
import itcast.blog.parser.VelogDataParser;
import itcast.blog.parser.YozmDataParser;
import itcast.blog.repository.BlogRepository;
import itcast.domain.blog.Blog;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

import static itcast.blog.constant.VelogQuery.VELOG_QUERY;
import static itcast.blog.constant.VelogQuery.VELOG_VARIABLES;

@Service
@Slf4j
@RequiredArgsConstructor
public class BlogCrawlService {

    private final VelogHttpClient velogHttpClient;
    private final VelogDataParser velogDataParser;
    private final YozmDataParser yozmDataParser;

    private final BlogRepository blogRepository;
    private final GPTService gptService;

    public void crawlVelog() {
        String jsonResponse = velogHttpClient.fetchTrendingPostsOfJson(VELOG_QUERY, VELOG_VARIABLES);
        List<String> blogUrls = velogDataParser.getBlogUrls(jsonResponse);

        List<String> filteredBlogUrls = filterAllLinks(blogUrls);
        List<Blog> blogs = velogDataParser.parseTrendingPosts(filteredBlogUrls);

        saveAndSendForGpt(blogs);
    }

    public void crawlYozm() {
        List<String> blogUrls = yozmDataParser.getBlogUrls();

        List<String> filteredBlogUrls = filterAllLinks(blogUrls);
        List<Blog> blogs = yozmDataParser.parseTrendingPosts(filteredBlogUrls);

        saveAndSendForGpt(blogs);
    }

    private List<String> filterAllLinks(List<String> blogUrls) {
        List<String> existingUrls = blogRepository.findAllLinks();
        return blogUrls.stream()
                .filter(blogUrl -> !existingUrls.contains(blogUrl))
                .toList();
    }

    private void saveAndSendForGpt(List<Blog> blogs) {
        blogs.forEach(blog -> {
                    Blog originalBlog = Blog.builder()
                            .platform(blog.getPlatform())
                            .title(blog.getTitle())
                            .originalContent(blog.getOriginalContent())
                            .publishedAt(blog.getPublishedAt())
                            .link(blog.getLink())
                            .thumbnail(blog.getThumbnail())
                            .status(blog.getStatus())
                            .build();
                    Blog savedId = blogRepository.save(originalBlog);
                    Message message = new Message("user", blog.getOriginalContent());
                    GPTSummaryRequest request = new GPTSummaryRequest("gpt-4o-mini", message, 0.7f);
                    gptService.updateBlogBySummaryContent(request, savedId.getId());
                }
        );
    }
}
