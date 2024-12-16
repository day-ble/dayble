package itcast.auth.application;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;

import itcast.auth.client.KakaoClient;
import itcast.auth.dto.response.KakaoUserInfo;
import itcast.auth.jwt.JwtUtil;
import itcast.domain.user.User;
import itcast.user.repository.UserRepository;
import jakarta.servlet.http.Cookie;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "KAKAO Login")
public class AuthService {

    private final KakaoClient kakaoClient;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    public String kakaoLogin(String code) throws JsonProcessingException {
        log.info("카카오 인증 코드 수신: {}", code);

        String accessToken = getToken(code);
        log.info("카카오로부터 액세스 토큰 수신: {}", accessToken);

        KakaoUserInfo kakaoUserInfo = getKakaoUserInfo(accessToken);
        log.info("카카오 사용자 정보 조회 성공: {}", kakaoUserInfo);

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

        log.info("최종 인증된 사용자 정보: ID = {}, Email = {}", user.getId(), user.getKakaoEmail());

        String jwtToken = jwtUtil.createToken(user.getId(), kakaoUserInfo.kakaoEmail());
        log.info("JWT 토큰 생성 완료: {}", jwtToken);

        return jwtToken;
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
