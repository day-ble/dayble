package itcast.news.repository;

import itcast.domain.news.News;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface NewsRepository extends JpaRepository<News, Long> {
    @Query("select n.link from News n")
    List<String> findAllLinks();

    @Query("select n from News n where function('DATE',n.createdAt) = :yesterday ")
    List<News> findAllByCreatedAt(@Param("yesterday") LocalDate yesterday);

    @Modifying
    @Query("DELETE FROM News n WHERE n.createdAt <= CURRENT_DATE - 6 MONTH")
    void deleteOldNews();
}
