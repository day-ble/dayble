package itcast.domain.mailEvent;

import itcast.domain.BaseEntity;
import itcast.domain.user.User;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MailEvents extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private String title;

    private String summary;

    private String originalLink;

    private String thumbnail;

    private MailEvents(
            final User user,
            final String title,
            final String summary,
            final String originalLink,
            final String thumbnail
    ) {
        this.user = user;
        this.title = title;
        this.summary = summary;
        this.originalLink = originalLink;
        this.thumbnail = thumbnail;
    }

    public static MailEvents of(
            final User user,
            final String title,
            final String summary,
            final String originalLink,
            final String thumbnail
    ) {
        return new MailEvents(user, title, summary, originalLink, thumbnail);
    }
}
