package com.dayble.blog.admin.controller.dto.request;

import com.dayble.blog.news.domain.News;
import com.dayble.blog.news.domain.enums.NewsStatus;
import com.dayble.blog.user.domain.enums.Interest;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record AdminNewsRequest(
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
    public static News toEntity(AdminNewsRequest adminNewsRequest) {
        return News.builder()
                .title(adminNewsRequest.title())
                .content(adminNewsRequest.content())
                .originalContent(adminNewsRequest.originalContent())
                .interest(adminNewsRequest.interest())
                .publishedAt(adminNewsRequest.publishedAt())
                .rating(adminNewsRequest.rating())
                .link(adminNewsRequest.link())
                .thumbnail(adminNewsRequest.thumbnail())
                .status(adminNewsRequest.status())
                .sendAt(adminNewsRequest.sendAt())
                .build();
    }
}
