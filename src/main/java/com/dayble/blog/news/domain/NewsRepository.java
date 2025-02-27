package com.dayble.blog.news.domain;

import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface NewsRepository extends JpaRepository<News, Long>, CustomNewsRepository {
   @Query("SELECT n.link FROM News n")
    List<String> findAllLinks();

    @Query("SELECT n FROM News n WHERE FUNCTION('DATE',n.createdAt) = :yesterday ORDER BY n.rating DESC LIMIT 3")
    List<News> findRatingTop3ByCreatedAt(@Param("yesterday") LocalDate yesterday);

    @Modifying
    @Query("DELETE FROM News n WHERE n.createdAt <= CURRENT_DATE - 6 MONTH")
    void deleteOldNews();

    @Query("SELECT n FROM News n WHERE n.sendAt IS NOT NULL")
    List<News> findAllBySendAt();
}
