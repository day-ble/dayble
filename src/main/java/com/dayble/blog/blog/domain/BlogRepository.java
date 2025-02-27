package com.dayble.blog.blog.domain;

import com.dayble.blog.blog.domain.enums.Platform;
import com.dayble.blog.user.domain.enums.Interest;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BlogRepository extends JpaRepository<Blog, Long>, CustomBlogRepository{
    @Query("SELECT b.link FROM Blog b")
    List<String> findAllLinks();

    @Query("""
            SELECT b FROM Blog b
            WHERE b.sendAt IS NULL
            AND b.publishedAt >= :recentDate
            AND b.rating >= :minRating
            AND b.platform = :platform
            AND b.interest = :interest
            ORDER BY b.rating DESC, b.publishedAt DESC
            """)
    List<Blog> findByBlogForSelection(Platform platform, Interest interest, LocalDate recentDate, int minRating, Pageable pageable);

    List<Blog> findAllBySendAtAndInterest(LocalDate today, Interest interest);
}
