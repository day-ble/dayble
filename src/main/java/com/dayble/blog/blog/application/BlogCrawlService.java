package com.dayble.blog.blog.application;

import static com.dayble.blog.blog.constant.VelogQuery.VELOG_QUERY;
import static com.dayble.blog.blog.constant.VelogQuery.VELOG_VARIABLES;
import static com.dayble.blog.global.exception.ErrorCodes.*;

import com.dayble.blog.ai.application.GPTService;
import com.dayble.blog.ai.dto.request.GPTSummaryRequest;
import com.dayble.blog.ai.dto.request.Message;
import com.dayble.blog.blog.application.client.VelogHttpClient;
import com.dayble.blog.blog.domain.Blog;
import com.dayble.blog.blog.domain.BlogRepository;
import com.dayble.blog.blog.parser.VelogDataParser;
import com.dayble.blog.blog.parser.YozmDataParser;
import com.dayble.blog.global.exception.ErrorCodes;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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
        try {
            String jsonResponse = velogHttpClient.fetchTrendingPostsOfJson(VELOG_QUERY, VELOG_VARIABLES);
            List<String> blogUrls = velogDataParser.getBlogUrls(jsonResponse);

            List<String> filteredBlogUrls = filterAllLinks(blogUrls);
            List<Blog> blogs = velogDataParser.parseTrendingPosts(filteredBlogUrls);

            saveAndSendForGpt(blogs);
        } catch (Exception e) {
            final ErrorCodes ex = BLOG_CRAWLING_ERROR;
            log.error("크롤링할 때 오류가 발생하였습니다. ErrorCode: {}, Message: {} Status: {}", ex.getCode(), ex.getMessage(),
                    ex.getStatus(), e);
        }
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
