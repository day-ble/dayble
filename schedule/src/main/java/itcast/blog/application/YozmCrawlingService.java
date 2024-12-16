package itcast.blog.application;

import itcast.ai.application.GPTService;
import itcast.ai.dto.request.GPTSummaryRequest;
import itcast.ai.dto.request.Message;
import itcast.blog.parser.YozmDataParser;
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
public class YozmCrawlingService {

    private final YozmDataParser yozmDataParser;
    private final BlogRepository blogRepository;
    private final GPTService gptService;

    public void crawlBlogs() {
        List<String> blogUrls = yozmDataParser.getBlogUrls();

        List<String> existingUrls = blogRepository.findAllLinks();
        List<String> filteredBlogUrls = blogUrls.stream()
                .filter(blogUrl -> !existingUrls.contains(blogUrl))
                .toList();

        List<Blog> blogs = yozmDataParser.parseTrendingPosts(filteredBlogUrls);

        blogs.forEach(blog -> {
                    Blog originalBlog = Blog.createYozmBlog(blog.getTitle(), blog.getOriginalContent(), blog.getPublishedAt(),
                            blog.getLink(), blog.getThumbnail());
                    Blog savedId = blogRepository.save(originalBlog);
                    Message message = new Message("user", blog.getOriginalContent());
                    GPTSummaryRequest request = new GPTSummaryRequest("gpt-4o-mini", message, 0.7f);
                    gptService.updateBlogBySummaryContent(request, savedId.getId());
                }
        );
    }

    @Scheduled(cron = "${scheduler.cron.crawling}")
    public void yozmCrawling() {
        log.info("Yozm Crawling Start ...");
        crawlBlogs();
        log.info("Yozm Crawling & Save!");
    }
}
