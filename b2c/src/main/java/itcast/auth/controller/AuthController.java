package itcast.auth.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import com.fasterxml.jackson.core.JsonProcessingException;

import itcast.ResponseTemplate;
import itcast.auth.application.AuthService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @GetMapping("/auth/kakao/callback")
    public ResponseTemplate<String> kakaoLogin(
            @RequestParam String code,
            HttpServletResponse response) throws JsonProcessingException {

        String jwtToken = authService.kakaoLogin(code);
        Cookie jwtCookie = authService.createJwtCookie(jwtToken);
        response.addCookie(jwtCookie);
        return new ResponseTemplate<>(HttpStatus.OK, "로그인되었습니다.");
    }
}