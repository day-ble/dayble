package itcast.application;

import itcast.domain.blog.Blog;
import itcast.domain.blog.enums.BlogStatus;
import itcast.domain.user.User;
import itcast.domain.user.enums.Interest;
import itcast.dto.request.AdminBlogRequest;
import itcast.dto.response.AdminBlogResponse;
import itcast.exception.IdNotFoundException;
import itcast.exception.NotAdminException;
import itcast.repository.AdminRepository;
import itcast.repository.BlogRepository;
import itcast.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.time.LocalDate;

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

    public Page<AdminBlogResponse> retrieveBlog(Long userId, BlogStatus blogStatus, Interest interest, LocalDate sendAt, int page, int size) {
        isAdmin(userId);
        Pageable pageable = PageRequest.of(page, size);
        return blogRepository.findBlogByCondition(blogStatus, interest, sendAt, pageable);
    }

    private void isAdmin(Long id){
        User user = userRepository.findById(id)
                .orElseThrow(()-> new IdNotFoundException("해당 유저가 존재하지 않습니다."));
        String email = user.getKakaoEmail();
        if (!adminRepository.existsByEmail(email)) {
            throw new NotAdminException("접근할 수 없는 유저입니다.");
        }
    }
}
