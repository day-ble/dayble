package itcast.controller;

import itcast.ResponseTemplate;
import itcast.application.AdminBlogService;
import itcast.domain.blog.enums.BlogStatus;
import itcast.domain.user.enums.Interest;
import itcast.dto.request.AdminBlogRequest;
import itcast.dto.response.AdminBlogResponse;
import itcast.dto.response.PageResponse;
import itcast.jwt.CheckAuth;
import itcast.jwt.LoginMember;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/blogs")
public class AdminBlogController {

    private final AdminBlogService adminBlogService;

    @GetMapping("/test")
    public String createBlog1(
    ) {
        return "테스트 어드민11111";
    }

    @CheckAuth
    @PostMapping
    public ResponseTemplate<AdminBlogResponse> createBlog(
            @LoginMember Long userId,
            @RequestBody AdminBlogRequest adminBlogRequest
    ) {
        AdminBlogResponse response = adminBlogService.createBlog(userId, adminBlogRequest);
        return new ResponseTemplate<>(HttpStatus.CREATED, "관리자 블로그 생성 성공", response);
    }

    @CheckAuth
    @GetMapping
    public ResponseTemplate<PageResponse<AdminBlogResponse>> retrieveBlogList(
            @LoginMember Long userId,
            @RequestParam(required = false) BlogStatus blogStatus,
            @RequestParam(required = false) Interest interest,
            @RequestParam(required = false) LocalDate startAt,
            @RequestParam(required = false) LocalDate endAt,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Page<AdminBlogResponse> blogPage = adminBlogService.retrieveBlogList(userId, blogStatus, interest, startAt, endAt, page, size);
        PageResponse<AdminBlogResponse> blogPageResponse = new PageResponse<>(
                blogPage.getContent(),
                blogPage.getNumber(),
                blogPage.getSize(),
                blogPage.getTotalPages()
        );
        return new ResponseTemplate<>(HttpStatus.OK, "관리자 블로그 리스트 조회 성공", blogPageResponse);
    }

    @CheckAuth
    @GetMapping("/{blogId}")
    public ResponseTemplate<AdminBlogResponse> retrieveBlog(
            @LoginMember Long userId,
            @PathVariable Long blogId
    ) {
        AdminBlogResponse response = adminBlogService.retrieveBlog(userId, blogId);
        return new ResponseTemplate<>(HttpStatus.OK, "관리자 블로그 단건 조회 성공", response);
    }

    @CheckAuth
    @PutMapping("/{blogId}")
    public ResponseTemplate<AdminBlogResponse> updateBlog(
            @LoginMember Long userId,
            @PathVariable Long blogId,
            @RequestBody AdminBlogRequest adminBlogRequest
    ){
        AdminBlogResponse response = adminBlogService.updateBlog(userId, blogId, adminBlogRequest);
        return new ResponseTemplate<>(HttpStatus.OK, "관리자 블로그 수정 성공", response);
    }

    @CheckAuth
    @DeleteMapping("/{blogId}")
    public ResponseTemplate<AdminBlogResponse> deleteBlog(
            @LoginMember Long userId,
            @PathVariable Long blogId
    ) {
        AdminBlogResponse response = adminBlogService.deleteBlog(userId, blogId);
        return new ResponseTemplate<>(HttpStatus.OK, "관리자 블로그 삭제 성공", response);
    }
}
