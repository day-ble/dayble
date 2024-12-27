package itcast.repository;

import itcast.domain.news.enums.NewsStatus;
import itcast.dto.response.AdminNewsResponse;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;

public interface CustomNewsRepository {
    Page<AdminNewsResponse> findNewsByCondition(NewsStatus status, LocalDate startAt, LocalDate endAt, Pageable pageable);
}
