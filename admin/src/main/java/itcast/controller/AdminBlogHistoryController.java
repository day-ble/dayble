package itcast.controller;

import itcast.ResponseTemplate;
import itcast.application.AdminBlogHistoryService;
import itcast.dto.response.AdminBlogHistoryResponse;
import itcast.dto.response.PageResponse;
import itcast.jwt.CheckAuth;
import itcast.jwt.LoginMember;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/blog-history")
public class AdminBlogHistoryController {

    private final AdminBlogHistoryService adminBlogHistoryService;

    @CheckAuth
    @GetMapping
    public ResponseTemplate<PageResponse<AdminBlogHistoryResponse>> retrieveBlogHistory(
            @LoginMember Long adminId,
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) Long blogId,
            @RequestParam(required = false) LocalDate createdAt,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        Page<AdminBlogHistoryResponse> blogHistoryPage = adminBlogHistoryService.retrieveBlogHistory(adminId, userId, blogId,
                createdAt, page, size);
        PageResponse<AdminBlogHistoryResponse> pageResponse = new PageResponse<>(
                blogHistoryPage.getContent(),
                blogHistoryPage.getNumber(),
                blogHistoryPage.getSize(),
                blogHistoryPage.getTotalPages()
        );
        return new ResponseTemplate<>(HttpStatus.OK, "관리자 블로그 히스토리 조회 성공", pageResponse);
    }
}
