package com.dayble.blog.admin.controller.dto.response;

import com.dayble.blog.newsHistory.domain.NewsHistory;
import java.time.LocalDateTime;

public record AdminNewsHistoryResponse (
        Long id,
        Long userId,
        Long newsId,
        LocalDateTime createdAt,
        LocalDateTime modifiedAt
) {
    public AdminNewsHistoryResponse(NewsHistory newsHistory) {
        this(
                newsHistory.getId(),
                newsHistory.getUser().getId(),
                newsHistory.getNews().getId(),
                newsHistory.getCreatedAt(),
                newsHistory.getModifiedAt()
        );
    }
}
