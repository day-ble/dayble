package itcast.dto.response;

import itcast.domain.blog.Blog;
import itcast.domain.blog.enums.BlogStatus;
import itcast.domain.blog.enums.Platform;
import itcast.domain.user.enums.Interest;
import java.time.LocalDateTime;

public record AdminBlogResponse(
        Long id,
        Platform platform,
        String title,
        String content,
        String originalContent,
        Interest interest,
        LocalDateTime publishedAt,
        int rating,
        String link,
        String thumbnail,
        BlogStatus status,
        LocalDateTime sendAt
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
