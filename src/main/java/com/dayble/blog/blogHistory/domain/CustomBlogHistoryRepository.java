package com.dayble.blog.blogHistory.domain;

import com.dayble.blog.admin.controller.dto.response.AdminBlogHistoryResponse;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomBlogHistoryRepository {
    Page<AdminBlogHistoryResponse> findBlogHistoryListByCondition(Long userId, Long blogId, LocalDate createdAt, Pageable pageable);
    List<AdminBlogHistoryResponse> downloadBlogHistoryListByCondition(Long userId, Long blogId, LocalDate startAt, LocalDate endAt);
}
