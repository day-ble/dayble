//package itcast.auth.application;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//import java.util.Optional;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.springframework.http.ResponseCookie;
//
//import itcast.auth.client.KakaoClient;
//import itcast.auth.dto.response.KakaoUserInfo;
//import itcast.jwt.JwtUtil;
//import itcast.domain.user.User;
//import itcast.jwt.repository.UserRepository;
//
//public class AuthServiceTest {
//
//    @Mock
//    private KakaoClient kakaoClient;
//
//    @Mock
//    private UserRepository userRepository;
//
//    @Mock
//    private JwtUtil jwtUtil;
//
//    @InjectMocks
//    private AuthService authService;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//    }
//
//    @Test
//    @DisplayName("새로운 사용자 로그인 시, USER에 저장된 후, JWT 토큰을 생성하고 반환한다.")
//    void test_newUser() throws Exception {
//        // Given
//        String code = "sampleCode";
//        String accessToken = "sampleAccessToken";
//        String jwtToken = "mockJwtToken";
//        KakaoUserInfo kakaoUserInfo = new KakaoUserInfo("newuser@naver.com");
//        User newUser = User.builder()
//                .id(1L)
//                .kakaoEmail("newuser@naver.com")
//                .build();
//
//        // Mocking
//        when(kakaoClient.getAccessToken(code)).thenReturn(accessToken);
//        when(kakaoClient.getKakaoUserInfo(accessToken)).thenReturn(kakaoUserInfo);
//        when(userRepository.findByKakaoEmail("newuser@naver.com")).thenReturn(Optional.empty());
//        when(userRepository.save(any(User.class))).thenReturn(newUser);
//        when(jwtUtil.createToken(anyLong(), anyString())).thenReturn("mockJwtToken");
//
//        // When
//        String token = authService.getAccessToken(code);
//        ResponseCookie cookie = ResponseCookie.from("Authorization", token)
//                .httpOnly(true)
//                .path("/")
//                .maxAge(3600)
//                .build();
//
//        // Then
//        assertNotNull(cookie);
//        assertEquals("Authorization", cookie.getName());
//        assertEquals(jwtToken, cookie.getValue());
//        verify(kakaoClient).getAccessToken(code);
//        verify(kakaoClient).getKakaoUserInfo(accessToken);
//        verify(userRepository).save(any(User.class));
//        verify(jwtUtil).createToken(anyLong(), anyString());
//    }
//
//    @Test
//    @DisplayName("기존 사용자 로그인 시, USER에 저장되지 않고, JWT 토큰만 생성하여 반환한다.")
//    void test_existingUser() throws Exception {
//        // Given
//        String code = "sampleCode";
//        String accessToken = "sampleAccessToken";
//        String jwtToken = "mockJwtToken";
//        KakaoUserInfo kakaoUserInfo = new KakaoUserInfo("user@naver.com");
//        User existingUser = User.builder()
//                .id(1L)
//                .kakaoEmail("user@naver.com")
//                .build();
//
//        // Mocking
//        when(kakaoClient.getAccessToken(code)).thenReturn(accessToken);
//        when(kakaoClient.getKakaoUserInfo(accessToken)).thenReturn(kakaoUserInfo);
//        when(userRepository.findByKakaoEmail("user@naver.com")).thenReturn(Optional.of(existingUser));
//        when(jwtUtil.createToken(anyLong(), anyString())).thenReturn("mockJwtToken");
//
//        // When
//        String token = authService.getAccessToken(code);
//        ResponseCookie cookie = ResponseCookie.from("Authorization", token)
//                .httpOnly(true)
//                .path("/")
//                .maxAge(3600)
//                .build();
//
//        // Then
//        assertNotNull(cookie);
//        assertEquals("Authorization", cookie.getName());
//        assertEquals(jwtToken, cookie.getValue());
//        verify(kakaoClient).getAccessToken(code);
//        verify(kakaoClient).getKakaoUserInfo(accessToken);
//        verify(userRepository).findByKakaoEmail("user@naver.com");
//        verify(userRepository, times(0)).save(any(User.class)); // 사용자 저장은 호출되지 않음
//        verify(jwtUtil).createToken(anyLong(), anyString());
//    }
//}
