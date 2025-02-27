package com.dayble.blog.ai.application;

import static com.dayble.blog.global.exception.ErrorCodes.BLOG_NOT_FOUND;
import static com.dayble.blog.global.exception.ErrorCodes.NEWS_NOT_FOUND;

import com.dayble.blog.ai.client.GPTClient;
import com.dayble.blog.ai.dto.request.GPTSummaryRequest;
import com.dayble.blog.ai.dto.response.GPTSummaryResponse;
import com.dayble.blog.blog.domain.Blog;
import com.dayble.blog.blog.domain.BlogRepository;
import com.dayble.blog.blog.domain.enums.BlogStatus;
import com.dayble.blog.global.exception.DaybleApplicationException;
import com.dayble.blog.news.domain.News;
import com.dayble.blog.news.domain.NewsRepository;
import com.dayble.blog.news.domain.enums.NewsStatus;
import com.dayble.blog.user.domain.enums.ArticleType;
import com.dayble.blog.user.domain.enums.Interest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class GPTService {

    private final GPTClient gptClient;
    private final BlogRepository blogRepository;
    private final NewsRepository newsRepository;

    @Transactional
    public void updateNewsBySummaryContent(final GPTSummaryRequest gptSummaryRequest, final Long newsId) {
        final GPTSummaryResponse response = gptClient.sendRequest(gptSummaryRequest, ArticleType.NEWS);

        final News news = newsRepository.findById(newsId)
                .orElseThrow(() -> new DaybleApplicationException(NEWS_NOT_FOUND));

        news.applySummaryUpdate(
                response.getSummary(),
                Interest.from(response.getCategory()),
                response.getRating(),
                NewsStatus.SUMMARY
        );
    }

    @Transactional
    public void updateBlogBySummaryContent(final GPTSummaryRequest gptSummaryRequest, final Long blogId) {
        final GPTSummaryResponse response = gptClient.sendRequest(gptSummaryRequest, ArticleType.BLOG);

        final Blog blog = blogRepository.findById(blogId)
                .orElseThrow(() -> new DaybleApplicationException(BLOG_NOT_FOUND));

        blog.applySummaryUpdate(
                response.getSummary(),
                Interest.from(response.getCategory()),
                response.getRating(),
                BlogStatus.SUMMARY
        );
    }
}
