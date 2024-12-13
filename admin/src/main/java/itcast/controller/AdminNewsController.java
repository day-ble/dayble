package itcast.controller;

import itcast.application.AdminNewsService;
import itcast.domain.news.enums.NewsStatus;
import itcast.dto.request.AdminNewsRequest;
import itcast.dto.response.AdminNewsResponse;
import itcast.ResponseTemplate;
import itcast.dto.response.PageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/news")
public class AdminNewsController {

    private final AdminNewsService adminService;

    @PostMapping
    public ResponseTemplate<AdminNewsResponse> createNews(@RequestParam Long userId, @RequestBody AdminNewsRequest adminNewsRequest) {
        AdminNewsResponse response = adminService.createNews(userId, adminNewsRequest.toEntity(adminNewsRequest));

        return new ResponseTemplate<>(HttpStatus.CREATED,"관리자 뉴스 생성 성공", response);
    }

    @GetMapping
    public ResponseTemplate<PageResponse<AdminNewsResponse>> retrieveNews(
            @RequestParam Long userId,
            @RequestParam(required = false) NewsStatus status,
            @RequestParam(required = false) LocalDate sendAt,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<AdminNewsResponse> newsPage = adminService.retrieveNews(userId, status, sendAt, page, size);
        PageResponse<AdminNewsResponse> newPageResponse = new PageResponse<>(
                newsPage.getContent(),
                newsPage.getNumber(),
                newsPage.getSize(),
                newsPage.getTotalPages()
        );
        return new ResponseTemplate<>(HttpStatus.OK, "관리자 뉴스 조회 성공", newPageResponse);
    }
}