package admin;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import com.dayble.blog.admin.application.AdminBlogService;
import com.dayble.blog.admin.controller.dto.request.AdminBlogRequest;
import com.dayble.blog.admin.controller.dto.response.AdminBlogResponse;
import com.dayble.blog.admin.domain.AdminRepository;
import com.dayble.blog.blog.domain.Blog;
import com.dayble.blog.blog.domain.BlogRepository;
import com.dayble.blog.blog.domain.enums.BlogStatus;
import com.dayble.blog.blog.domain.enums.Platform;
import com.dayble.blog.user.domain.User;
import com.dayble.blog.user.domain.UserRepository;
import com.dayble.blog.user.domain.enums.Interest;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
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
    public void successBlogCreate() {
        //given
        Long userId = 1L;
        LocalDate fixedTime = LocalDate.of(2024, 12, 1);

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
    @DisplayName("블로그 다건 조회 성공")
    public void successBlogListRetrieve() {
        // Given
        Long userId = 1L;
        LocalDate startAt = LocalDate.of(2024, 12, 1);
        LocalDate endAt = LocalDate.of(2024, 12, 2);
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
                        LocalDate.now(),
                        5,
                        "http://link1.com",
                        "http:thumb1.com",
                        BlogStatus.SUMMARY,
                        LocalDate.of(2024, 12, 1)),
                new AdminBlogResponse(
                        2L,
                        Platform.VELOG,
                        "블로그2",
                        "요약내용2",
                        "원본내용2",
                        Interest.BACKEND,
                        LocalDate.now(),
                        5,
                        "http://link2.com",
                        "http:thumb2.com",
                        BlogStatus.SUMMARY,
                        LocalDate.of(2024, 12, 1))
        );

        Pageable pageable = PageRequest.of(page, size);
        Page<AdminBlogResponse> blogPage = new PageImpl<>(responses, pageable, responses.size());

        given(userRepository.findById(userId)).willReturn(Optional.of(user));
        given(adminRepository.existsByEmail(user.getKakaoEmail())).willReturn(true);
        given(blogRepository.findBlogByCondition(status, interest, startAt, endAt, pageable)).willReturn(blogPage);

        //when
        Page<AdminBlogResponse> responsePage = adminBlogService.retrieveBlogList(userId, status, interest, startAt, endAt, page, size);

        //then
        assertEquals(2, responsePage.getContent().size());
        assertEquals("블로그1", responsePage.getContent().get(0).title());
        assertEquals("블로그2", responsePage.getContent().get(1).title());
        assertEquals(page, responsePage.getNumber());
        assertEquals(size, responsePage.getSize());
        verify(blogRepository).findBlogByCondition(status, interest, startAt, endAt, pageable);
    }

    @Test
    @DisplayName("블로그 단건 조회 성공")
    public void successBlogRetrieve() {
        //given
        Long userId = 1L;
        Long blogId = 1L;
        LocalDate fixedTime = LocalDate.of(2024, 12, 1);

        User user = User.builder()
                .id(1L)
                .kakaoEmail("kakao@kakao.com")
                .build();
        Blog blog = Blog.adminBuilder()
                .id(1L)
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

        given(userRepository.findById(userId)).willReturn(Optional.of(user));
        given(adminRepository.existsByEmail(user.getKakaoEmail())).willReturn(true);
        given(blogRepository.findById(blogId)).willReturn(Optional.of(blog));

        //when
        AdminBlogResponse response = adminBlogService.retrieveBlog(userId, blogId);

        //Then
        assertEquals(blog.getTitle(), response.title());
        assertEquals(blog.getSendAt(), response.sendAt());
        verify(blogRepository).findById(blogId);
    }

    @Test
    @DisplayName("블로그 수정 성공")
    public void successBlogUpdate() {
        //given
        Long userId = 1L;
        Long blogId = 1L;
        LocalDate fixedTime = LocalDate.of(2024, 12, 1);

        User user = User.builder()
                .id(1L)
                .kakaoEmail("kakao@kakao.com")
                .build();

        Blog blog = Blog.adminBuilder()
                .id(1L)
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
                "제목2",
                "수정본2",
                "원본2",
                Interest.NEWS,
                fixedTime,
                3,
                "http://example2.com",
                "http://thumbnail2.com",
                BlogStatus.ORIGINAL,
                fixedTime
        );

        given(userRepository.findById(userId)).willReturn(Optional.of(user));
        given(adminRepository.existsByEmail(user.getKakaoEmail())).willReturn(true);
        given(blogRepository.findById(blogId)).willReturn(Optional.of(blog));

        // when
        AdminBlogResponse response = adminBlogService.updateBlog(userId, blogId, adminBlogRequest);

        // Then
        assertEquals("제목2", response.title());
        assertEquals(BlogStatus.ORIGINAL, response.status());
    }

    @Test
    @DisplayName("블로그 삭제 성공")
    public void successBlogDelete(){
        //Given
        Long userId = 1L;
        Long blogId = 1L;

        User adminUser = User.builder()
                .id(userId)
                .kakaoEmail("admin@kakao.com")
                .build();

        Blog blog = Blog.adminBuilder()
                .id(1L)
                .title("테스트 블로그")
                .content("테스트 내용")
                .build();

        given(userRepository.findById(userId)).willReturn(Optional.of(adminUser));
        given(adminRepository.existsByEmail(adminUser.getKakaoEmail())).willReturn(true);
        given(blogRepository.findById(blogId)).willReturn(Optional.of(blog));

        // When
        AdminBlogResponse response = adminBlogService.deleteBlog(userId, blogId);

        // Then
        assertEquals(blog.getId(), response.id());
        assertEquals(blog.getTitle(), response.title());
        verify(blogRepository).delete(blog);
    }
}
