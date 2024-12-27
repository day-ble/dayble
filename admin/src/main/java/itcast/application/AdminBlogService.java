package itcast.application;

import itcast.domain.blog.Blog;
import itcast.domain.blog.enums.BlogStatus;
import itcast.domain.user.User;
import itcast.domain.user.enums.Interest;
import itcast.dto.request.AdminBlogRequest;
import itcast.dto.response.AdminBlogResponse;
import itcast.exception.ErrorCodes;
import itcast.exception.ItCastApplicationException;
import itcast.jwt.repository.UserRepository;
import itcast.repository.AdminRepository;
import itcast.repository.BlogRepository;
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
                new ItCastApplicationException(ErrorCodes.BLOG_NOT_FOUND));
        return new AdminBlogResponse(blog);
    }

    @Transactional
    public AdminBlogResponse updateBlog(Long userId, Long blogId, AdminBlogRequest adminBlogRequest) {
        isAdmin(userId);
        Blog blog = blogRepository.findById(blogId).orElseThrow(() ->
                new ItCastApplicationException(ErrorCodes.BLOG_NOT_FOUND));
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
                new ItCastApplicationException(ErrorCodes.BLOG_NOT_FOUND));
        blogRepository.delete(blog);

        return new AdminBlogResponse(blog);
    }

    private void isAdmin(Long id) {
        User user = userRepository.findById(id).orElseThrow(() ->
                new ItCastApplicationException(ErrorCodes.USER_NOT_FOUND));
        String email = user.getKakaoEmail();
        if (!adminRepository.existsByEmail(email)) {
            throw new ItCastApplicationException(ErrorCodes.INVALID_USER);
        }
    }
}
