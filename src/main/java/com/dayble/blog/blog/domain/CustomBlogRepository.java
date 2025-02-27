package com.dayble.blog.blog.domain;

import com.dayble.blog.admin.controller.dto.response.AdminBlogResponse;
import com.dayble.blog.blog.domain.enums.BlogStatus;
import com.dayble.blog.user.domain.enums.Interest;
import java.time.LocalDate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomBlogRepository {
    Page<AdminBlogResponse> findBlogByCondition(BlogStatus status, Interest interest, LocalDate startAt, LocalDate endAt, Pageable pageable);
}
