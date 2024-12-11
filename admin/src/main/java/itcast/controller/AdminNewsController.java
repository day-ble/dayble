package itcast.controller;

import itcast.application.AdminNewsService;
import itcast.dto.request.AdminNewsRequest;
import itcast.dto.response.AdminNewsResponse;
import itcast.ResponseTemplate;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class AdminNewsController {

    private final AdminNewsService adminService;

    @PostMapping("/news")
    public ResponseTemplate<AdminNewsResponse> createNews(@RequestParam Long userId, @RequestBody AdminNewsRequest adminNewsRequest) {
        AdminNewsResponse response = adminService.createNews(userId, adminNewsRequest.toEntity(adminNewsRequest));

        return new ResponseTemplate<>(HttpStatus.CREATED,"관리자 뉴스 생성 성공", response);
    }
}
