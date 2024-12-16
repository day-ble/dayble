package itcast;

import itcast.application.AdminBlogService;
import itcast.domain.blog.Blog;
import itcast.domain.blog.enums.BlogStatus;
import itcast.domain.blog.enums.Platform;
import itcast.domain.news.News;
import itcast.domain.user.User;
import itcast.domain.user.enums.Interest;
import itcast.dto.request.AdminBlogRequest;
import itcast.dto.response.AdminBlogResponse;
import itcast.repository.AdminRepository;
import itcast.repository.BlogRepository;
import itcast.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class AdminBlogServiceTest {
    @Mock
    private BlogRepository blogRepository;
    @Mock
    private AdminRepository adminRepository;
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private AdminBlogService adminBlogService;

    @Test
    @DisplayName("블로그 생성 성공")
    public void SuccessBlogCreate() {
        //given
        Long userId = 1L;
        LocalDateTime fixedTime = LocalDateTime.of(2024, 12, 1, 12, 0);

        User user = User.builder()
                .id(1L)
                .kakaoEmail("kakao@kakao.com")
                .build();

        Blog blog = Blog.adminBuilder()
                .platform(Platform.VELOG)
                .title("제목")
                .content("수정본")
                .originalContent("원본")
                .interest(Interest.BACKEND)
                .publishedAt(fixedTime)
                .rating(5)
                .link("http://example.com")
                .thumbnail("http://thumbnail.com")
                .status(BlogStatus.SUMMARY)
                .sendAt(fixedTime)
                .build();
        AdminBlogRequest adminBlogRequest = new AdminBlogRequest(
                Platform.VELOG,
                "제목",
                "수정본",
                "원본",
                Interest.BACKEND,
                fixedTime,
                5,
                "http://example.com",
                "http://thumbnail.com",
                BlogStatus.SUMMARY,
                fixedTime
        );

        given(userRepository.findById(userId)).willReturn(Optional.of(user));
        given(adminRepository.existsByEmail(user.getKakaoEmail())).willReturn(true);
        given(blogRepository.save(any(Blog.class))).willReturn(blog);

        //when
        AdminBlogResponse response = adminBlogService.createBlog(userId, adminBlogRequest);

        //then
        assertEquals(blog.getTitle(), response.title());
        assertEquals(blog.getSendAt(), response.sendAt());
        verify(blogRepository).save(any(Blog.class));
    }

    @Test
    @DisplayName("블로그 조회 성공")
    public void SuccessBlogRetrieve() {
        // Given
        Long userId = 1L;
        LocalDate sendAt = LocalDate.of(2024, 12, 1);
        BlogStatus status = BlogStatus.SUMMARY;
        Interest interest = Interest.BACKEND;
        int page = 0;
        int size = 10;

        User user = User.builder()
                .id(userId)
                .kakaoEmail("admin@kakao.com")
                .build();

        List<AdminBlogResponse> responses = List.of(
                new AdminBlogResponse(
                        1L,
                        Platform.VELOG,
                        "블로그1",
                        "요약내용1",
                        "원본내용1",
                        Interest.BACKEND,
                        LocalDateTime.now(),
                        5,
                        "http://link1.com",
                        "http:thumb1.com",
                        BlogStatus.SUMMARY,
                        LocalDateTime.of(2024, 12, 1, 13, 0)),
                new AdminBlogResponse(
                        2L,
                        Platform.VELOG,
                        "블로그2",
                        "요약내용2",
                        "원본내용2",
                        Interest.BACKEND,
                        LocalDateTime.now(),
                        5,
                        "http://link2.com",
                        "http:thumb2.com",
                        BlogStatus.SUMMARY,
                        LocalDateTime.of(2024, 12, 1, 13, 0))
        );

        Pageable pageable = PageRequest.of(page, size);
        Page<AdminBlogResponse> blogPage = new PageImpl<>(responses, pageable, responses.size());

        given(userRepository.findById(userId)).willReturn(Optional.of(user));
        given(adminRepository.existsByEmail(user.getKakaoEmail())).willReturn(true);
        given(blogRepository.findBlogByCondition(status, interest, sendAt, pageable)).willReturn(blogPage);

        //when
        Page<AdminBlogResponse> responsePage = adminBlogService.retrieveBlog(userId, status, interest, sendAt, page, size);

        //then
        assertEquals(2, responsePage.getContent().size());
        assertEquals("블로그1", responsePage.getContent().get(0).title());
        assertEquals("블로그2", responsePage.getContent().get(1).title());
        assertEquals(page, responsePage.getNumber());
        assertEquals(size, responsePage.getSize());
        verify(blogRepository).findBlogByCondition(status, interest, sendAt,  pageable);
    }
}
