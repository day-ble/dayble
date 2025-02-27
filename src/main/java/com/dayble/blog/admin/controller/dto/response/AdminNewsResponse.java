package com.dayble.blog.admin.controller.dto.response;

import com.dayble.blog.news.domain.News;
import com.dayble.blog.news.domain.enums.NewsStatus;
import com.dayble.blog.user.domain.enums.Interest;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record AdminNewsResponse(
        Long id,
        String title,
        String content,
        String originalContent,
        Interest interest,
        LocalDateTime publishedAt,
        Integer rating,
        String link,
        String thumbnail,
        NewsStatus status,
        LocalDate sendAt
) {
    public AdminNewsResponse(News news) {
        this(
                news.getId(),
                news.getTitle(),
                news.getContent(),
                news.getOriginalContent(),
                news.getInterest(),
                news.getPublishedAt(),
                news.getRating(),
                news.getLink(),
                news.getThumbnail(),
                news.getStatus(),
                news.getSendAt());
    }
}
