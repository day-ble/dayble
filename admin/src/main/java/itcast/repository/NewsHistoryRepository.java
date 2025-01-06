package itcast.repository;

import itcast.domain.newsHistory.NewsHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface NewsHistoryRepository extends JpaRepository<NewsHistory, Long>, CustomNewsHistoryRepository {
    @Modifying
    @Query(value = "DELETE FROM news_history where is_dummy = true", nativeQuery = true)
    void deleteAllDummyData();
}
