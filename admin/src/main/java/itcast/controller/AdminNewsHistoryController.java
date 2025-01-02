package itcast.controller;

import itcast.ResponseTemplate;
import itcast.application.AdminNewsHistoryService;

import itcast.dto.response.AdminNewsHistoryResponse;
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
@RequestMapping("/api/news-history")
public class AdminNewsHistoryController {

    private final AdminNewsHistoryService adminNewsHistoryService;

    @CheckAuth
    @GetMapping
    public ResponseTemplate<PageResponse<AdminNewsHistoryResponse>> retrieveNewsHistory(
            @LoginMember Long adminId,
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) Long newsId,
            @RequestParam(required = false) LocalDate createdAt,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        Page<AdminNewsHistoryResponse> newsHistoryPage
                = adminNewsHistoryService.retrieveNewsHistory(adminId, userId, newsId, createdAt, page, size);
        PageResponse<AdminNewsHistoryResponse> pageResponse = new PageResponse<>(
                newsHistoryPage.getContent(),
                newsHistoryPage.getNumber(),
                newsHistoryPage.getSize(),
                newsHistoryPage.getTotalPages()
        );
        return new ResponseTemplate<>(HttpStatus.OK, "관리자 뉴스 히스토리 조회 성공", pageResponse);
    }
}
