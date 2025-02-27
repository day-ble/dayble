package com.dayble.blog.auth.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {

    @GetMapping("/")
    public String subscriptionPage() {
        return "main";
    }

    @GetMapping("/auth/kakao/subscribe")
    public String kakaoLogin() {
        return "subscribe";
    }

    @GetMapping("/subscribe-page")
    public String kakaoLogin1() {
        return "subscribe-page";
    }
}
