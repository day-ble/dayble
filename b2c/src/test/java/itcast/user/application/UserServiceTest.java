package itcast.user.application;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import itcast.domain.user.User;
import itcast.domain.user.enums.ArticleType;
import itcast.domain.user.enums.Interest;
import itcast.domain.user.enums.SendingType;
import itcast.user.dto.request.ProfileCreateRequest;
import itcast.user.dto.response.ProfileCreateResponse;
import itcast.user.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @InjectMocks
    private UserService userService;
    @Mock
    private UserRepository userRepository;

    @Test
    @DisplayName("해당하는 id를 찾아 id와 kakaoemail은 유지한채 회원정보를 저장한다.")
    void createProfile_Success() {
        // Given
        Long userId = 1L;
        ProfileCreateRequest request = new ProfileCreateRequest(
                "nickname",
                ArticleType.NEWS,
                Interest.NEWS,
                SendingType.EMAIL,
                "test@example.com"
        );

        User existingUser = User.builder()
                .id(userId)
                .kakaoEmail("kakaoemail@example.com")
                .build();

        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
        when(userRepository.existsByNickname(request.nickname())).thenReturn(false);
        when(userRepository.existsByEmail(request.email())).thenReturn(false);
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        ProfileCreateResponse response = userService.createProfile(request, userId);

        // Then
        assertEquals(userId, response.id());
        assertEquals("nickname", response.nickname());
        assertEquals(ArticleType.NEWS, response.articleType());
        assertEquals(Interest.NEWS, response.interest());
        assertEquals(SendingType.EMAIL, response.sendingType());
        assertEquals("test@example.com", response.email());

        verify(userRepository).findById(userId);
        verify(userRepository).existsByNickname(request.nickname());
        verify(userRepository).existsByEmail(request.email());
        verify(userRepository).save(any(User.class));
    }

    @Test
    @DisplayName("ArticleType이 NEWS일때 Interest는 항상 NEWS로 저장된다.")
    void createProfile_interestIsNewsWhenArticleTypeIsNews() {
        // Given
        Long userId = 1L;
        ProfileCreateRequest request = new ProfileCreateRequest(
                "nickname",
                ArticleType.NEWS,
                Interest.FRONTEND,
                SendingType.EMAIL,
                "test@example.com"
        );

        User existingUser = User.builder()
                .id(userId)
                .kakaoEmail("kakaoemail@example.com")
                .build();

        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User savedUser = invocation.getArgument(0);
            assertEquals(ArticleType.NEWS, savedUser.getArticleType());
            assertEquals(Interest.NEWS, savedUser.getInterest());
            return savedUser;
        });

        // When
        userService.createProfile(request, userId);

        // Then
        verify(userRepository).findById(userId);
        verify(userRepository).save(any(User.class));
    }
}
