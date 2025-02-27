package com.dayble.blog.admin.controller.dto.response;

import com.dayble.blog.blog.domain.Blog;
import com.dayble.blog.blog.domain.enums.BlogStatus;
import com.dayble.blog.blog.domain.enums.Platform;
import com.dayble.blog.user.domain.enums.Interest;
import java.time.LocalDate;

public record AdminBlogResponse(
        Long id,
        Platform platform,
        String title,
        String content,
        String originalContent,
        Interest interest,
        LocalDate publishedAt,
        int rating,
        String link,
        String thumbnail,
        BlogStatus status,
        LocalDate sendAt
) {
    public AdminBlogResponse(Blog blog) {
        this(
                blog.getId(),
                blog.getPlatform(),
                blog.getTitle(),
                blog.getContent(),
                blog.getOriginalContent(),
                blog.getInterest(),
                blog.getPublishedAt(),
                blog.getRating(),
                blog.getLink(),
                blog.getThumbnail(),
                blog.getStatus(),
                blog.getSendAt());
    }
}
