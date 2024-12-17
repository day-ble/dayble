package itcast.auth.application;

import java.util.Optional;
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
public class AuthService {

    private final KakaoClient kakaoClient;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    public String getAccessToken(String code) throws JsonProcessingException {
        String accessToken = getToken(code);
        KakaoUserInfo kakaoUserInfo = getKakaoUserInfo(accessToken);

        Optional<User> existingUser = userRepository.findByKakaoEmail(kakaoUserInfo.kakaoEmail());
        User user = existingUser.orElseGet(() -> {
            User newUser = User.builder()
                    .kakaoEmail(kakaoUserInfo.kakaoEmail())
                    .build();
            return userRepository.save(newUser);
        });
        return jwtUtil.createToken(user.getId(), kakaoUserInfo.kakaoEmail());
    }

    private String getToken(String code) {
        return kakaoClient.getAccessToken(code);
    }

    private KakaoUserInfo getKakaoUserInfo(String accessToken) throws JsonProcessingException {
        return kakaoClient.getKakaoUserInfo(accessToken);
    }
}