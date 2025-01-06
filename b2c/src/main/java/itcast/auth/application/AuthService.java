package itcast.auth.application;

import jakarta.servlet.http.Cookie;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.core.JsonProcessingException;
import itcast.auth.client.KakaoClient;
import itcast.auth.dto.response.KakaoUserInfo;
import itcast.jwt.JwtUtil;
import itcast.domain.user.User;
import itcast.jwt.repository.UserRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "KAKAO Login")
public class AuthService {

    private final KakaoClient kakaoClient;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    public String kakaoLogin(String code) throws JsonProcessingException {
        String accessToken = getToken(code);

        KakaoUserInfo kakaoUserInfo = getKakaoUserInfo(accessToken);

        Optional<User> existingUser = userRepository.findByKakaoEmail(kakaoUserInfo.kakaoEmail());
        if (existingUser.isPresent()) {
            log.info("기존 사용자 발견: ID = {}, Email = {}",
                    existingUser.get().getId(), existingUser.get().getKakaoEmail());
        } else {
            log.info("기존 사용자가 없습니다. 이메일: {}. 새로운 사용자 생성 중...", kakaoUserInfo.kakaoEmail());
        }

        User user = existingUser.orElseGet(() -> {
            User newUser = User.builder()
                    .kakaoEmail(kakaoUserInfo.kakaoEmail())
                    .build();
            User savedUser = userRepository.save(newUser);
            log.info("새 사용자 저장 완료: ID = {}", savedUser.getId());
            return savedUser;
        });

        return jwtUtil.createToken(user.getId(), kakaoUserInfo.kakaoEmail());
    }

    private String getToken(String code) throws JsonProcessingException {
        return kakaoClient.getAccessToken(code);
    }

    private KakaoUserInfo getKakaoUserInfo(String accessToken) throws JsonProcessingException {
        return kakaoClient.getKakaoUserInfo(accessToken);
    }

    public Cookie createJwtCookie(String jwtToken) {
        Cookie cookie = new Cookie(JwtUtil.AUTHORIZATION_HEADER, jwtToken);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(3600);
        return cookie;
    }
}
