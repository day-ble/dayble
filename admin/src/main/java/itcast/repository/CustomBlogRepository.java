package itcast.repository;

import itcast.domain.blog.enums.BlogStatus;
import itcast.domain.user.enums.Interest;
import itcast.dto.response.AdminBlogResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;

public interface CustomBlogRepository {
    Page<AdminBlogResponse> findBlogByCondition(BlogStatus status, Interest interest, LocalDate sendAt, Pageable pageable);
}
