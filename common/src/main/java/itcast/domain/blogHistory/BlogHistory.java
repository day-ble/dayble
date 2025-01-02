package itcast.domain.blogHistory;

import itcast.domain.BaseEntity;
import itcast.domain.blog.Blog;
import itcast.domain.user.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "blog_history", indexes = {@Index(name = "idx_user_news_created", columnList = "user_id, blog_id, created_at")})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BlogHistory extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "blog_id")
    private Blog blog;

    @Builder
    public BlogHistory(User user, Blog blog) {
        this.user = user;
        this.blog = blog;
    }
}
