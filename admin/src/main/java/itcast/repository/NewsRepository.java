package itcast.repository;

import itcast.domain.news.News;

import org.springframework.data.jpa.repository.JpaRepository;

public interface NewsRepository extends JpaRepository<News, Long>, CustomNewsRepository {
}
