package itcast.user.application;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import itcast.domain.user.User;
import itcast.domain.user.enums.ArticleType;
import itcast.domain.user.enums.Interest;
import itcast.domain.user.enums.SendingType;
import itcast.jwt.repository.UserRepository;
import itcast.user.dto.request.ProfileCreateRequest;
import itcast.user.dto.request.ProfileUpdateRequest;
import itcast.user.dto.response.ProfileCreateResponse;
import itcast.user.dto.response.ProfileUpdateResponse;

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

    @Test
    @DisplayName("회원정보를 성공적으로 수정한다.")
    public void updateProfile_Success() {
        // Given
        Long userId = 1L;
        ProfileUpdateRequest request = new ProfileUpdateRequest(
                "newNickname",
                ArticleType.NEWS,
                Interest.NEWS,
                SendingType.EMAIL,
                "newEmail@example.com"
        );

        User existingUser = User.builder()
                .id(userId)
                .nickname("oldNickname")
                .email("oldEmail@example.com")
                .build();

        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
        when(userRepository.existsByNickname(request.nickname())).thenReturn(false);
        when(userRepository.existsByEmail(request.email())).thenReturn(false);
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        ProfileUpdateResponse response = userService.updateProfile(request, userId);

        // Then
        assertNotNull(response);
        assertEquals("newNickname", response.nickname());
        assertEquals("newEmail@example.com", response.email());
        assertEquals(Interest.NEWS, response.interest());
    }

    @Test
    @DisplayName("회원정보 수정 시 일부 필드가 null 또는 빈 공백일 경우, 해당 필드는 기존 정보를 유지한다.")
    public void updateProfile_WithPartialFields_Success() {
        // Given
        Long userId = 1L;
        ProfileUpdateRequest request = new ProfileUpdateRequest(
                "",
                null,
                null,
                SendingType.EMAIL,
                ""
        );

        // 기존 사용자 정보
        User existingUser = User.builder()
                .id(userId)
                .nickname("oldNickname")
                .articleType(ArticleType.NEWS)
                .interest(Interest.NEWS)
                .sendingType(SendingType.KAKAO)
                .email("oldEmail@example.com")
                .build();

        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
        when(userRepository.existsByNickname(request.nickname())).thenReturn(false);
        when(userRepository.existsByEmail(request.email())).thenReturn(false);
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        ProfileUpdateResponse response = userService.updateProfile(request, userId);

        // Then
        assertNotNull(response);
        assertEquals("oldNickname", response.nickname());
        assertEquals("oldEmail@example.com", response.email());
        assertEquals(ArticleType.NEWS, response.articleType());
        assertEquals(Interest.NEWS, response.interest());
        assertEquals(SendingType.EMAIL, response.sendingType());
    }
}