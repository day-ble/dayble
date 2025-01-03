package itcast.repository;

import itcast.dto.response.AdminBlogHistoryResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

public interface CustomBlogHistoryRepository {
    Page<AdminBlogHistoryResponse> findBlogHistoryListByCondition(Long userId, Long blogId, LocalDate createdAt, Pageable pageable);
    List<AdminBlogHistoryResponse> downloadBlogHistoryListByCondition(Long userId, Long blogId, LocalDate startAt, LocalDate endAt);
}
