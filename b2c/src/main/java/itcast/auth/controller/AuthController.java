package itcast.auth.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import itcast.auth.application.AuthService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @GetMapping("/auth/kakao/callback")
    public String kakaoLogin(
            @RequestParam String code,
            HttpServletResponse response) throws JsonProcessingException {

        String jwtToken = authService.kakaoLogin(code);
        Cookie jwtCookie = authService.createJwtCookie(jwtToken);
        System.out.println(jwtCookie.getValue());
        System.out.println(jwtCookie.getName());
        response.addCookie(jwtCookie);
        return "redirect:main";
    }
}
