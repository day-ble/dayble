package itcast.repository;

import itcast.domain.news.News;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface NewsRepository extends JpaRepository<News, Long> {
}
