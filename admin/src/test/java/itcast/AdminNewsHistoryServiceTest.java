package itcast;

import itcast.domain.news.News;
import itcast.domain.newsHistory.NewsHistory;
import itcast.domain.user.User;
import itcast.dto.response.AdminNewsHistoryResponse;
import itcast.jwt.repository.UserRepository;
import itcast.repository.NewsHistoryRepository;
import itcast.repository.NewsRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StopWatch;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest(classes = AdminApplication.class)
public class AdminNewsHistoryServiceTest {

    @Autowired
    NewsHistoryRepository newsHistoryRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    NewsRepository newsRepository;

    @BeforeEach
    @DisplayName("10만개 더미데이터 생성")
    void makeDummyData() {
        List<User> users = userRepository.findAll();
        List<News> newsList = newsRepository.findAll();
        List<NewsHistory> newsHistories = new ArrayList<>();

        for (long i = 0; i < 100000; i++) {
            User user = users.get((int) (Math.random() * users.size()));
            News news = newsList.get((int) (Math.random() * newsList.size()));

            NewsHistory newsHistory = NewsHistory.builder()
                    .user(user)
                    .news(news)
                    .isDummy(true)
                    .build();
            newsHistories.add(newsHistory);

            if (newsHistories.size() % 1000 == 0) {
                newsHistoryRepository.saveAll(newsHistories);
                newsHistories.clear();
            }
        }

        if (!newsHistories.isEmpty()) {
            newsHistoryRepository.saveAll(newsHistories);
        }
    }

    @Test
    @DisplayName("히스토리 조회 성공")
    public void successNewsHistoryRetrieve() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        // Given
        Long userId = 1L;
        Long newsId = 2L;
        LocalDate createdAt = LocalDate.now();
        int page = 0;
        int size = 20;
        Pageable pageable = PageRequest.of(page, size);

        // When
        Page<AdminNewsHistoryResponse> newsHistories = newsHistoryRepository.findNewsHistoryListByCondition(userId, newsId, createdAt, pageable);

        // Then
        assertNotNull(newsHistories);
        assertFalse(newsHistories.isEmpty());

        stopWatch.stop();
        System.out.println("걸린 시간: " + stopWatch.getTotalTimeMillis() + " ms");
    }

    @AfterEach
    @DisplayName("더미데이터 삭제")
    void deleteDummyData() {
        newsHistoryRepository.deleteAllDummyData();
    }
}