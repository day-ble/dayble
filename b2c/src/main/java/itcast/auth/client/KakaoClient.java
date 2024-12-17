package itcast.auth.client;

import java.util.Objects;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import itcast.auth.dto.response.AccessTokenResponse;
import itcast.auth.dto.response.KakaoUserInfo;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j(topic = "KAKAO Login")
public class KakaoClient {

    private final WebClient tokenClient; // Access Token 요청 전용
    private final WebClient userInfoClient; // 사용자 정보 요청 전용

    @Value("${spring.kakao.client-id}")
    private String clientId;
    @Value("${spring.kakao.redirect-uri}")
    private String redirectUri;

    public KakaoClient() {
        this.tokenClient = WebClient.builder()
                .baseUrl("https://kauth.kakao.com")
                .build();

        this.userInfoClient = WebClient.builder()
                .baseUrl("https://kapi.kakao.com")
                .build();
    }

    public String getAccessToken(String code) {
        log.info("인가코드 : {}", code);
        String accessTokenUri = "/oauth/token";

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", clientId);
        body.add("redirect_uri", redirectUri);
        body.add("code", code);

        try {
            AccessTokenResponse accessTokenResponse = tokenClient.post()
                    .uri(accessTokenUri)
                    .header(HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=utf-8")
                    .bodyValue(body)
                    .retrieve()
                    .bodyToMono(AccessTokenResponse.class)
                    .block();

            return Objects.requireNonNull(accessTokenResponse).accessToken();
        } catch (Exception e) {
            log.error("Access token 처리 중 오류 발생: {}", e.getMessage());
            throw new RuntimeException("Access token 처리 중 오류가 발생했습니다.", e);
        }
    }

    public KakaoUserInfo getKakaoUserInfo(String accessToken) {
        log.info("accessToken : {}", accessToken);
        String userInfoUri = "/v2/user/me";

        String responseBody = userInfoClient.get()
                .uri(userInfoUri)
                .headers(headers -> headers.setBearerAuth(accessToken))
                .retrieve()
                .bodyToMono(String.class)
                .block();

        try {
            JsonNode jsonNode = new ObjectMapper().readTree(responseBody);
            String email = jsonNode.path("kakao_account").path("email").asText();
            log.info("카카오 사용자 이메일: {}", email);
            return new KakaoUserInfo(email);
        } catch (JsonProcessingException e) {
            log.error("카카오 사용자 정보 파싱 오류: ", e);
            throw new RuntimeException("카카오 사용자 정보를 받아오는 중 오류가 발생했습니다.", e);
        }
    }
}