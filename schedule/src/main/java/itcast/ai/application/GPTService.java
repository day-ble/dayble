package itcast.ai.application;

import static itcast.exception.ErrorCodes.BLOG_NOT_FOUND;

import itcast.ai.client.GPTClient;
import itcast.ai.dto.request.GPTSummaryRequest;
import itcast.ai.dto.response.GPTSummaryResponse;
import itcast.domain.blog.Blog;
import itcast.domain.blog.enums.BlogStatus;
import itcast.domain.user.enums.Interest;
import itcast.exception.ItCastApplicationException;
import itcast.news.repository.BlogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class GPTService {

    private final GPTClient gptClient;
    private final BlogRepository blogRepository;

    @Transactional
    public void updateBlogBySummaryContent(final GPTSummaryRequest gptSummaryRequest) {
        final GPTSummaryResponse response = gptClient.sendRequest(gptSummaryRequest);

        final Blog blog = blogRepository.findById(1L)
                .orElseThrow(() -> new ItCastApplicationException(BLOG_NOT_FOUND));

//        blog.applySummaryUpdate(
//                response.getSummary(),
//                Interest.from(response.getCategory()),
//                response.getRating(),
//                BlogStatus.SUMMARY
//        );
    }
}
