package itcast.dto.request;

import itcast.domain.news.News;
import itcast.domain.news.enums.NewsStatus;
import itcast.domain.user.enums.Interest;
import java.time.LocalDateTime;

public record AdminNewsRequest (
        String title,
        String content,
        String originalContent,
        Interest interest,
        LocalDateTime publishedAt,
        int rating,
        String link,
        String thumbnail,
        NewsStatus status,
        LocalDateTime sendAt
){
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