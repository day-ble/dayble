package itcast;

import itcast.application.AdminBlogHistoryService;
import itcast.domain.blog.Blog;
import itcast.domain.blogHistory.BlogHistory;
import itcast.domain.user.User;
import itcast.dto.response.AdminBlogHistoryResponse;
import itcast.jwt.repository.UserRepository;
import itcast.repository.BlogHistoryRepository;
import itcast.repository.BlogRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest(classes = AdminApplication.class)
public class AdminBlogHistoryServiceTest {

    @Autowired
    BlogHistoryRepository blogHistoryRepository;

    @Test
    @DisplayName("블로그 히스토리 조회 성공")
    public void SuccessBlogHistoryRetrieve() {
        //given
        Long userId = 1L;
        Long blogId = null;
        LocalDate createdAt = null;
        int page = 0;
        int size = 20;
        Pageable pageable = PageRequest.of(page, size);

        //when
        Page<AdminBlogHistoryResponse> blogHistories = blogHistoryRepository.findBlogHistoryListByCondition(userId, blogId, createdAt, pageable);

        //Then
        assertNotNull(blogHistories);
        assertFalse(blogHistories.isEmpty());
        assertEquals(userId, blogHistories.getContent().get(0).userId());
    }
}