package com.dayble.blog.admin.controller.dto.response;

import com.dayble.blog.blogHistory.domain.BlogHistory;
import java.time.LocalDateTime;

public record AdminBlogHistoryResponse (
        Long id,
        Long userId,
        Long blogId,
        LocalDateTime createdAt,
        LocalDateTime modifiedAt
) {
    public AdminBlogHistoryResponse(BlogHistory blogHistory) {
        this(
                blogHistory.getId(),
                blogHistory.getUser().getId(),
                blogHistory.getBlog().getId(),
                blogHistory.getCreatedAt(),
                blogHistory.getModifiedAt()
        );
    }
}
