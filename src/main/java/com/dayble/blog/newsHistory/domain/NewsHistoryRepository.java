package com.dayble.blog.newsHistory.domain;

import org.springframework.data.jpa.repository.JpaRepository;

public interface NewsHistoryRepository extends JpaRepository<NewsHistory, Long>, CustomNewsHistoryRepository {
}
