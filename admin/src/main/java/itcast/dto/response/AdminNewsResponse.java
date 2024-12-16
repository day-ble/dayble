package itcast.dto.response;

import itcast.domain.news.News;
import itcast.domain.news.enums.NewsStatus;
import itcast.domain.user.enums.Interest;
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
        LocalDateTime sendAt
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
