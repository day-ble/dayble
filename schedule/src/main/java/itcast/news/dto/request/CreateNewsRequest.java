package itcast.news.dto.request;

import itcast.domain.news.News;
import itcast.domain.news.enums.NewsStatus;
import itcast.domain.user.enums.Interest;

import java.time.LocalDate;
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
