package com.dayble.blog.admin.controller;

import com.dayble.blog.global.ResponseTemplate;
import com.dayble.blog.admin.application.AdminNewsService;
import com.dayble.blog.admin.controller.dto.request.AdminNewsRequest;
import com.dayble.blog.admin.controller.dto.response.AdminNewsResponse;
import com.dayble.blog.admin.controller.dto.response.PageResponse;
import com.dayble.blog.global.interceptor.CheckAuth;
import com.dayble.blog.global.interceptor.LoginMember;
import com.dayble.blog.news.domain.enums.NewsStatus;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/news")
public class AdminNewsController {

    private final AdminNewsService adminNewsService;

    @CheckAuth
    @PostMapping
    public ResponseTemplate<AdminNewsResponse> createNews(
            @LoginMember Long userId,
            @RequestBody AdminNewsRequest adminNewsRequest
    ) {
        AdminNewsResponse response = adminNewsService.createNews(userId, adminNewsRequest);
        return new ResponseTemplate<>(HttpStatus.CREATED,"관리자 뉴스 생성 성공", response);
    }

    @CheckAuth
    @GetMapping
    public ResponseTemplate<PageResponse<AdminNewsResponse>> retrieveNews(
            @LoginMember Long userId,
            @RequestParam(required = false) NewsStatus status,
            @RequestParam(required = false) LocalDate startAt,
            @RequestParam(required = false) LocalDate endAt,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Page<AdminNewsResponse> newsPage = adminNewsService.retrieveNewsList(userId, status, startAt, endAt, page, size);
        PageResponse<AdminNewsResponse> newPageResponse = new PageResponse<>(
                newsPage.getContent(),
                newsPage.getNumber(),
                newsPage.getSize(),
                newsPage.getTotalPages()
        );
        return new ResponseTemplate<>(HttpStatus.OK, "관리자 뉴스 리스트 조회 성공", newPageResponse);
    }

    @CheckAuth
    @GetMapping("/{newsId}")
    public ResponseTemplate<AdminNewsResponse> retrieveNews(
            @LoginMember Long userId,
            @PathVariable Long newsId
    ) {
        AdminNewsResponse response = adminNewsService.retrieveNews(userId, newsId);
        return new ResponseTemplate<>(HttpStatus.OK, "관리자 뉴스 단건 조회 성공", response);
    }

    @CheckAuth
    @PutMapping("/{newsId}")
    public ResponseTemplate<AdminNewsResponse> updateNews(
            @LoginMember Long userId,
            @PathVariable Long newsId,
            @RequestBody AdminNewsRequest adminNewsRequest
    ) {
        AdminNewsResponse response = adminNewsService.updateNews(userId, newsId, adminNewsRequest);
        return new ResponseTemplate<>(HttpStatus.OK, "관리자 뉴스 수정 성공", response);
    }

    @CheckAuth
    @DeleteMapping("/{newsId}")
    public ResponseTemplate<AdminNewsResponse> deleteNews(
            @LoginMember Long userId,
            @PathVariable Long newsId
    ) {
        AdminNewsResponse response = adminNewsService.deleteNews(userId, newsId);
        return new ResponseTemplate<>(HttpStatus.OK, "관리자 뉴스 삭제 성공", response);
    }
}
