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

    @Query("select n from News n where function('DATE',n.createdAt) = :yesterday order by n.rating desc limit 3")
    List<News> findRatingTot3ByCreatedAtOrdarByRating(@Param("yesterday") LocalDate yesterday);

    @Modifying
    @Query("DELETE FROM News n WHERE n.createdAt <= CURRENT_DATE - 6 MONTH")
    void deleteOldNews();

    @Query("select n.title, n.content, n.link, n.thumbnail from News n where n.sendAt is not null")
    List<News> findAllBySendAt();
}
