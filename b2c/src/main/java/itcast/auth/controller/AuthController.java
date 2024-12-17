package itcast.auth.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import com.fasterxml.jackson.core.JsonProcessingException;

import itcast.ResponseTemplate;
import itcast.auth.application.AuthService;
import itcast.jwt.JwtUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @GetMapping("/auth/kakao/callback")
    public ResponseTemplate<String> kakaoLogin(@RequestParam String code) {
        return new ResponseTemplate<>(HttpStatus.OK, "인증 코드가 발급되었습니다: " + code);
    }

    @PostMapping("/auth/kakao/token")
    public ResponseTemplate<String> getAccessToken(
            @RequestParam String code,
            HttpServletResponse response) throws JsonProcessingException {
        String jwtToken = authService.getAccessToken(code);
        Cookie cookie = new Cookie(JwtUtil.AUTHORIZATION_HEADER, jwtToken);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(3600);
        response.addCookie(cookie);
        return new ResponseTemplate<>(HttpStatus.OK, "로그인되었습니다.");
    }
}
