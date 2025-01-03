package itcast.domain.newsHistory;

import itcast.domain.BaseEntity;
import itcast.domain.news.News;
import itcast.domain.user.User;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Index;
import jakarta.persistence.Column;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "news_history", indexes = {
@Index(name = "idx_user_news_created", columnList = "user_id, news_id, created_at")})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class NewsHistory extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "news_id")
    private News news;

    @Builder
    public NewsHistory(User user, News news) {
        this.user = user;
        this.news = news;
    }

    @Builder()
    public NewsHistory(Long id, User user, News news) {}
}
