package com.dayble.blog.blogHistory.domain;

import com.dayble.blog.global.BaseEntity;
import com.dayble.blog.blog.domain.Blog;
import com.dayble.blog.user.domain.User;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "blog_history", indexes = {
        @Index(name = "idx_user_news_created", columnList = "user_id, blog_id, created_at")})
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
