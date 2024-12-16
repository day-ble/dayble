package itcast.controller;

import itcast.ResponseTemplate;
import itcast.application.AdminBlogService;
import itcast.dto.request.AdminBlogRequest;
import itcast.dto.response.AdminBlogResponse;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class AdminBlogController {

    private final AdminBlogService adminService;

    @PostMapping("/blogs")
    public ResponseTemplate<AdminBlogResponse> createBlog(
            @RequestParam Long userId,
            @RequestBody AdminBlogRequest adminBlogRequest
    ) {
        AdminBlogResponse response = adminService.createBlog(userId, adminBlogRequest.toEntity(adminBlogRequest));
        return new ResponseTemplate<>(HttpStatus.CREATED, "관리자 블로그 생성 성공", response);
    }
}
