package itcast.blog.repository;

import itcast.domain.blogHistory.BlogHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BlogHistoryRepository extends JpaRepository<BlogHistory, Long> {
}
