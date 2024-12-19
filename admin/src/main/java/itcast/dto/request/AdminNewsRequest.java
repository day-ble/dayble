package itcast.dto.request;

import itcast.domain.news.News;
import itcast.domain.news.enums.NewsStatus;
import itcast.domain.user.enums.Interest;

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
