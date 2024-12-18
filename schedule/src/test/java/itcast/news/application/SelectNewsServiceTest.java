package itcast.news.application;

import itcast.domain.news.News;
import itcast.domain.user.User;
import itcast.domain.user.enums.Interest;
import itcast.jwt.repository.UserRepository;
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
import java.time.temporal.ChronoUnit;
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

    @Test
    @DisplayName("retrieveUserEmails 메소드 테스트")
    public void retrieveUserEmailsTest() {
        // give
        Interest validInterest = Interest.NEWS;

        User mockUser1 = mock(User.class);
        when(mockUser1.getEmail()).thenReturn("user1@example.com");

        User mockUser2 = mock(User.class);
        when(mockUser2.getEmail()).thenReturn("user2@example.com");

        List<User> users = List.of(mockUser1, mockUser2);
        when(userRepository.findAllByInterest(validInterest)).thenReturn(users);

        // when
        List<String> result = sendNewsService.retrieveUserEmails(validInterest);

        // then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.contains("user1@example.com"));
        assertTrue(result.contains("user2@example.com"));
        verify(userRepository, times(1)).findAllByInterest(validInterest);
        verify(mockUser1, times(1)).getEmail();
        verify(mockUser2, times(1)).getEmail();
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
        ArgumentCaptor<LocalDateTime> captor = ArgumentCaptor.forClass(LocalDateTime.class);

        verify(mockNews1, times(1)).newsUpdate(captor.capture());
        LocalDateTime actualSendAt1 = captor.getValue();
        assertEquals(expectedSendAt.truncatedTo(ChronoUnit.SECONDS), actualSendAt1.truncatedTo(ChronoUnit.SECONDS));

        verify(mockNews2, times(1)).newsUpdate(captor.capture());
        LocalDateTime actualSendAt2 = captor.getValue();
        assertEquals(expectedSendAt.truncatedTo(ChronoUnit.SECONDS), actualSendAt2.truncatedTo(ChronoUnit.SECONDS));

    }
}
