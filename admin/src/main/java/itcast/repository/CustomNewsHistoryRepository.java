package itcast.repository;

import itcast.domain.newsHistory.NewsHistory;
import itcast.dto.response.AdminNewsHistoryResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

public interface CustomNewsHistoryRepository {
    Page<AdminNewsHistoryResponse> findNewsHistoryListByCondition(Long userId, Long newsId, LocalDate createdAt, Pageable pageable);
    List<AdminNewsHistoryResponse> downloadNewsHistoryListByCondition(Long userId, Long newsId, LocalDate startAt, LocalDate endAt);
}
