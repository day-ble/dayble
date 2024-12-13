package itcast.application;

import itcast.domain.blog.Blog;
import itcast.domain.user.User;
import itcast.dto.response.AdminBlogResponse;
import itcast.exception.IdNotFoundException;
import itcast.exception.NotAdminException;
import itcast.repository.AdminRepository;
import itcast.repository.BlogRepository;
import itcast.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.naming.AuthenticationException;

@Service
@RequiredArgsConstructor
public class AdminBlogService {

    private final BlogRepository blogRepository;
    private final UserRepository userRepository;
    private final AdminRepository adminRepository;

    public AdminBlogResponse createBlog(Long userId, Blog blog) {
        isAdmin(userId);
        Blog savedBlogs = blogRepository.save(blog);
        return new AdminBlogResponse(savedBlogs);
    }

    private void isAdmin(Long id){
        User user = userRepository.findById(id).orElseThrow(()->
                new IdNotFoundException("해당 유저 id가 존재하지 않습니다."));
        String email = user.getKakaoEmail();
        if(!adminRepository.existsByEmail(email)){
            throw new NotAdminException("접근할 수 없는 유저입니다.");
        }
    }
}
