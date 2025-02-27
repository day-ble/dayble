package com.dayble.blog.news.controller.request;

import com.dayble.blog.news.domain.News;
import com.dayble.blog.news.domain.enums.NewsStatus;
import com.dayble.blog.user.domain.enums.Interest;
import java.time.LocalDateTime;

public record CreateNewsRequest(
        String title,
        String originalContent,
        String link,
        String thumbnail,
        LocalDateTime publishedAt
) {
    public News toEntity(
            String title,
            String originalContent,
            String link,
            String thumbnail,
            LocalDateTime publishedAt
    ) {
        return News.builder()
                .title(title)
                .originalContent(originalContent)
                .link(link)
                .interest(Interest.NEWS)
                .status(NewsStatus.ORIGINAL)
                .thumbnail(thumbnail)
                .publishedAt(publishedAt)
                .build();
    }
}
