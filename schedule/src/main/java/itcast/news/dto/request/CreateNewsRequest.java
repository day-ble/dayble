package itcast.news.dto.request;

import itcast.domain.news.News;

import java.time.LocalDateTime;

public record CreateNewsRequest(
        String title,
        String originalContent,
        String link,
        String thumbnail,
        LocalDateTime sendAt
) {
    public News toEntity(
            String title, String originalContent, String link, String thumbnail, LocalDateTime publishedAt){
        return News.builder()
                .title(title)
                .originalContent(originalContent)
                .link(link)
                .thumbnail(thumbnail)
                .publishedAt(publishedAt)
                .build();
    }
}
