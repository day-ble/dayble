package com.dayble.blog.news.domain;

import com.dayble.blog.admin.controller.dto.response.AdminNewsResponse;
import com.dayble.blog.news.domain.enums.NewsStatus;
import java.time.LocalDate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomNewsRepository {
    Page<AdminNewsResponse> findNewsByCondition(NewsStatus status, LocalDate startAt, LocalDate endAt, Pageable pageable);
}
