package itcast.blog.application;

import itcast.blog.parser.YozmDataParser;
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
public class YozmCrawlingService {

    private final YozmDataParser yozmDataParser;
    private final BlogRepository blogRepository;

    public List<Blog> crawlBlogs() {
        List<String> blogUrls = yozmDataParser.getBlogUrls();

        List<String> existingUrls = blogRepository.findAllLinks();
        List<String> filteredBlogUrls = blogUrls.stream()
                .filter(blogUrl -> !existingUrls.contains(blogUrl))
                .toList();

        List<Blog> blogs = yozmDataParser.parseTrendingPosts(filteredBlogUrls);
        return blogs;
    }

    @Scheduled(cron = "${scheduler.cron.crawling}")
    public void yozmCrawling() {
        log.info("Yozm Crawling Start ...");

        final List<Blog> blogs = crawlBlogs();
        blogRepository.saveAll(blogs);

        log.info("Yozm Crawling & Save!");
    }
}
