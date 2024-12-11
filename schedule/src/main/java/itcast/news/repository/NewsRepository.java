package itcast.news.repository;

import itcast.domain.news.News;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface NewsRepository extends JpaRepository<News, Long> {
   @Query("select n.link from News n")
    List<String> findAllLinks();
}
