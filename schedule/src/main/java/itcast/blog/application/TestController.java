package itcast.blog.application;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test")
public class TestController {

    @GetMapping("/test")
    public String createBlog1111(
    ) {
        return "테스트 스케줄11111";
    }
}
