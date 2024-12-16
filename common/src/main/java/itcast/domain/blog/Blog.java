package itcast.domain.blog;

import itcast.domain.BaseEntity;
import itcast.domain.blog.enums.BlogStatus;
import itcast.domain.blog.enums.Platform;
import itcast.domain.user.enums.Interest;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;

import java.time.LocalDateTime;

import lombok.AccessLevel;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Blog extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private Platform platform;

    @Column(nullable = false)
    private String title;

    @Lob
    private String content;

    @Lob
    @Column(nullable = false, columnDefinition = "TEXT")
    private String originalContent;

    @Enumerated(EnumType.STRING)
    private Interest interest;

    @Column(nullable = false)
    private LocalDateTime publishedAt;

    private Integer rating;

    @Column(nullable = false)
    private String link;

    private String thumbnail;

    @Enumerated(EnumType.STRING)
    private BlogStatus status;

    private LocalDateTime sendAt;

    @Builder
    public Blog(
            Platform platform,
            String title,
            String originalContent,
            LocalDateTime publishedAt,
            String link,
            String thumbnail,
            BlogStatus status
    ) {
        this.platform = platform;
        this.title = title;
        this.originalContent = originalContent;
        this.publishedAt = publishedAt;
        this.link = link;
        this.thumbnail = thumbnail;
        this.status = status;
    }

    @Builder
    public Blog(Platform platform, String title, String content, String originalContent, Interest interest,
                LocalDateTime publishedAt, int rating, String link, String thumbnail, BlogStatus status, LocalDateTime sendAt) {
        this.platform = platform;
        this.title = title;
        this.content = content;
        this.originalContent = originalContent;
        this.interest = interest;
        this.publishedAt = publishedAt;
        this.rating = rating;
        this.link = link;
        this.thumbnail = thumbnail;
        this.status = status;
        this.sendAt = sendAt;
    }

/*    public void applySummaryUpdate(
            final String content,
            final Interest interest,
            final Integer rating,
            final BlogStatus status
    ) {
        this.content = content;
        this.interest = interest;
        this.rating = rating;
        this.status = status;
    }*/
}
