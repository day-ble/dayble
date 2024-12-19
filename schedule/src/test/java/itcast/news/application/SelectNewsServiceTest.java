package itcast.news.application;

import itcast.domain.news.News;
import itcast.domain.user.User;
import itcast.domain.user.enums.Interest;
import itcast.jwt.repository.UserRepository;
import itcast.mail.application.MailService;
import itcast.mail.dto.request.SendMailRequest;
import itcast.news.repository.NewsRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class SelectNewsServiceTest {
    private static final int YESTERDAY = 1;
    private static final int ALARM_HOUR = 2;
    private static final int ALARM_DAY = 2;

    @Mock
    private UserRepository userRepository;

    @Mock
    private NewsRepository newsRepository;

    @InjectMocks
    private SendNewsService sendNewsService;

    @Mock
    private MailService mailService;

    @Test
    @DisplayName("retrieveUserEmails 메소드 테스트")
    public void retrieveUserEmailsTest() {
        // give
        Interest validInterest = Interest.NEWS;

        String mockUser1 = "user1@example.com";
        String mockUser2 = "user2@example.com";

        List<String> users = List.of(mockUser1, mockUser2);
        when(userRepository.findAllByInterest(validInterest)).thenReturn(users);

        // when
        List<String> result = sendNewsService.retrieveUserEmails(validInterest);

        // then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.contains("user1@example.com"));
        assertTrue(result.contains("user2@example.com"));
        verify(userRepository, times(1)).findAllByInterest(validInterest);
    }

    @Test
    @DisplayName("selectNews 메소드 테스트")
    public void selectNewsTest() {
        // give
        LocalDate yesterday = LocalDate.now().minusDays(YESTERDAY);
        LocalDateTime expectedSendAt = LocalDateTime.now().plusDays(ALARM_DAY).plusHours(ALARM_HOUR);

        News mockNews1 = mock(News.class);
        News mockNews2 = mock(News.class);
        List<News> mockNewsList = List.of(mockNews1, mockNews2);

        when(newsRepository.findRatingTot3ByCreatedAtOrdarByRating(yesterday)).thenReturn(mockNewsList);

        // when
        sendNewsService.selectNews();

        // then
        ArgumentCaptor<LocalDate> captor = ArgumentCaptor.forClass(LocalDate.class);

        verify(mockNews1, times(1)).newsUpdate(captor.capture());
        verify(mockNews2, times(1)).newsUpdate(captor.capture());


    }

    @Test
    @DisplayName("sendNews 메소드 테스트")
    public void sendNewsTest() {
        News news1 = News.builder()
                .id(1L)
                .title("Test Title 1")
                .content("Test Content 1")
                .link("http://link1.com")
                .thumbnail("http://thumbnail1.com")
                .sendAt(LocalDate.now())
                .build();

        News news2 = News.builder()
                .id(2L)
                .title("Test Title 2")
                .content("Test Content 2")
                .link("http://link2.com")
                .thumbnail("http://thumbnail2.com")
                .sendAt(LocalDate.now())
                .build();

        // 이메일 리스트 Mock 데이터
        List<String> emails = List.of("test1@example.com", "test2@example.com");

        when(newsRepository.findAllBySendAt()).thenReturn(List.of(news1, news2)); // 뉴스 반환
        when(sendNewsService.retrieveUserEmails(Interest.NEWS)).thenReturn(emails); // 이메일 반환

        // when: 메소드 실행
        sendNewsService.sendNews();

        // then: 메일 전송 호출 여부 확인
        verify(mailService, times(1)).send(any(SendMailRequest.class));
    }


}
