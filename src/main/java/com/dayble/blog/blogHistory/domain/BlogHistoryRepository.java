package com.dayble.blog.blogHistory.domain;

import org.springframework.data.jpa.repository.JpaRepository;

public interface BlogHistoryRepository extends JpaRepository<BlogHistory, Long>, CustomBlogHistoryRepository {
}
