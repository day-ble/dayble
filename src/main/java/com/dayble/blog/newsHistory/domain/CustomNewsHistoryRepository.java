package com.dayble.blog.newsHistory.domain;

import com.dayble.blog.admin.controller.dto.response.AdminNewsHistoryResponse;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomNewsHistoryRepository {
    Page<AdminNewsHistoryResponse> findNewsHistoryListByCondition(Long userId, Long newsId, LocalDate createdAt, Pageable pageable);
    List<AdminNewsHistoryResponse> downloadNewsHistoryListByCondition(Long userId, Long newsId, LocalDate startAt, LocalDate endAt);
}
