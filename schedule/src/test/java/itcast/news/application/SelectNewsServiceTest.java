package itcast.news.application;

import itcast.domain.user.User;
import itcast.domain.user.enums.Interest;
import itcast.jwt.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class SelectNewsServiceTest {

    @Mock
    private UserRepository userRepository;

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
}
