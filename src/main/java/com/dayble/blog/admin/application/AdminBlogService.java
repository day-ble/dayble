package com.dayble.blog.admin.application;

import com.dayble.blog.admin.controller.dto.request.AdminBlogRequest;
import com.dayble.blog.admin.controller.dto.response.AdminBlogResponse;
import com.dayble.blog.admin.domain.AdminRepository;
import com.dayble.blog.blog.domain.Blog;
import com.dayble.blog.blog.domain.BlogRepository;
import com.dayble.blog.blog.domain.enums.BlogStatus;
import com.dayble.blog.global.exception.DaybleApplicationException;
import com.dayble.blog.global.exception.ErrorCodes;
import com.dayble.blog.user.domain.User;
import com.dayble.blog.user.domain.UserRepository;
import com.dayble.blog.user.domain.enums.Interest;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AdminBlogService {

    private final BlogRepository blogRepository;
    private final UserRepository userRepository;
    private final AdminRepository adminRepository;

    public AdminBlogResponse createBlog(Long userId, AdminBlogRequest adminBlogRequest) {
        isAdmin(userId);
        Blog blog = AdminBlogRequest.toEntity(adminBlogRequest);
        Blog savedBlogs = blogRepository.save(blog);
        return new AdminBlogResponse(savedBlogs);
    }

    public Page<AdminBlogResponse> retrieveBlogList(
            Long userId,
            BlogStatus blogStatus,
            Interest interest,
            LocalDate startAt,
            LocalDate endAt,
            int page,
            int size
    ) {
        isAdmin(userId);
        Pageable pageable = PageRequest.of(page, size);
        return blogRepository.findBlogByCondition(blogStatus, interest, startAt, endAt, pageable);
    }

    public AdminBlogResponse retrieveBlog(Long userId, Long blogId) {
        isAdmin(userId);
        Blog blog = blogRepository.findById(blogId).orElseThrow(() ->
                new DaybleApplicationException(ErrorCodes.BLOG_NOT_FOUND));
        return new AdminBlogResponse(blog);
    }

    @Transactional
    public AdminBlogResponse updateBlog(Long userId, Long blogId, AdminBlogRequest adminBlogRequest) {
        isAdmin(userId);
        Blog blog = blogRepository.findById(blogId).orElseThrow(() ->
                new DaybleApplicationException(ErrorCodes.BLOG_NOT_FOUND));
        blog.update(
                adminBlogRequest.platform(),
                adminBlogRequest.title(),
                adminBlogRequest.content(),
                adminBlogRequest.originalContent(),
                adminBlogRequest.interest(),
                adminBlogRequest.publishedAt(),
                adminBlogRequest.rating(),
                adminBlogRequest.link(),
                adminBlogRequest.thumbnail(),
                adminBlogRequest.status(),
                adminBlogRequest.sendAt()
        );
        return new AdminBlogResponse(blog);
    }

    public AdminBlogResponse deleteBlog(Long userId, Long blogId) {
        isAdmin(userId);
        Blog blog = blogRepository.findById(blogId).orElseThrow(() ->
                new DaybleApplicationException(ErrorCodes.BLOG_NOT_FOUND));
        blogRepository.delete(blog);

        return new AdminBlogResponse(blog);
    }

    private void isAdmin(Long id) {
        User user = userRepository.findById(id).orElseThrow(() ->
                new DaybleApplicationException(ErrorCodes.USER_NOT_FOUND));
        String email = user.getKakaoEmail();
        if (!adminRepository.existsByEmail(email)) {
            throw new DaybleApplicationException(ErrorCodes.INVALID_USER);
        }
    }
}
