package itcast.ai.application;

import static itcast.exception.ErrorCodes.BLOG_NOT_FOUND;
import static itcast.exception.ErrorCodes.NEWS_NOT_FOUND;

import itcast.ai.client.GPTClient;
import itcast.ai.dto.request.GPTSummaryRequest;
import itcast.ai.dto.response.GPTSummaryResponse;
import itcast.blog.repository.BlogRepository;
import itcast.domain.blog.Blog;
import itcast.domain.blog.enums.BlogStatus;
import itcast.domain.news.News;
import itcast.domain.news.enums.NewsStatus;
import itcast.domain.user.enums.ArticleType;
import itcast.domain.user.enums.Interest;
import itcast.exception.ItCastApplicationException;
import itcast.blog.repository.BlogRepository;
import itcast.news.repository.NewsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static itcast.exception.ErrorCodes.BLOG_NOT_FOUND;

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
                .orElseThrow(() -> new ItCastApplicationException(NEWS_NOT_FOUND));

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
                .orElseThrow(() -> new ItCastApplicationException(BLOG_NOT_FOUND));

//        blog.applySummaryUpdate(
//                response.getSummary(),
//                Interest.from(response.getCategory()),
//                response.getRating(),
//                BlogStatus.SUMMARY
//        );
    }
}
