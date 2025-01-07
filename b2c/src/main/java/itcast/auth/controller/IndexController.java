package itcast.auth.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {

    @GetMapping("/")
    public String subscriptionPage() {
        return "main";
    }

    @GetMapping("/subscribe")
    public String kakaoLogin() {
        return "subscribe";
    }
}
