package itcast.ai.application;

import itcast.ai.Message;
import itcast.ai.client.GPTClient;
import itcast.ai.dto.request.GPTSummaryRequest;
import itcast.ai.dto.response.GPTSummaryResponse;
import itcast.blog.repository.BlogRepository;
import itcast.domain.blog.Blog;
import itcast.domain.blog.enums.BlogStatus;
import itcast.domain.user.enums.Interest;
import itcast.exception.ItCastApplicationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class GPTServiceTest {

    @Mock
    private GPTClient gptClient;

    @Mock
    private BlogRepository blogRepository;

    @InjectMocks
    private GPTService gptService;

    @Test
    @DisplayName("올바른 요청이 들어오면 요약에 성공한다.")
    void summaryContent_success_test() {
        // given
        final Long blogId = 1L;
        final String originalContent = "test originalContent";

        final Blog blog = mock(Blog.class);

        final Message message = new Message("user", originalContent);

        final GPTSummaryRequest gptSummaryRequest = new GPTSummaryRequest("gpt-4o-mini", message, 0.7f);

        final String jsonResponse = "{\n" +
                "  \"category\" : \"BACKEND\",\n" +
                "  \"summary\" : \"test summary\",\n" +
                "  \"rating\" : 8\n" +
                "}";

        GPTSummaryResponse.Message messages = new GPTSummaryResponse.Message(jsonResponse);
        GPTSummaryResponse.Choice choice = new GPTSummaryResponse.Choice(messages);
        GPTSummaryResponse response = new GPTSummaryResponse(Collections.singletonList(choice));

        when(gptClient.sendRequest(gptSummaryRequest)).thenReturn(response);
        ;
        when(blogRepository.findById(blogId)).thenReturn(Optional.of(blog));

        // when
        gptService.updateBlogBySummaryContent(gptSummaryRequest);

        // then
        verify(blog, times(1)).applySummaryUpdate(any(), any(), any(), any());
        verify(blog, times(1)).applySummaryUpdate(eq("test summary"), eq(Interest.BACKEND), eq(8L),
                eq(BlogStatus.SUMMARY));
        verify(gptClient, times(1)).sendRequest(gptSummaryRequest);
    }

    @Test
    @DisplayName("블로그가 유효하지 않으면 요약에 실패한다.")
    void summaryContent_test_not_found_blog() {
        // given
        final Long blogId = 1L;
        final String originalContent = "test originalContent";

        final Message message = new Message("user", originalContent);

        final GPTSummaryRequest gptSummaryRequest = new GPTSummaryRequest("gpt-4o-mini", message, 0.7f);

        when(blogRepository.findById(blogId)).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> gptService.updateBlogBySummaryContent(gptSummaryRequest))
                .isInstanceOf(ItCastApplicationException.class);
    }
}
