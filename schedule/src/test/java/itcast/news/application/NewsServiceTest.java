package itcast.news.application;

import itcast.news.repository.NewsRepository;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class NewsServiceTest {

    @InjectMocks
    private NewsService newsService;

    @Mock
    private NewsRepository newsRepository;

    @Test
    @DisplayName("크롤링 테스트")
    void SuccessCrawling() throws IOException {
        // give
        String url = "https://news.naver.com/breakingnews/section/105/283";
        Document document = Jsoup.connect(url).get();

        // when
        Elements articles = document.select(".sa_thumb_inner");

        // then
        assertFalse(articles.isEmpty(), "링크가 없음");
    }

    @Test
    @DisplayName("중복 된 메소드 체크 테스트")
    void isValidLinksTest() {
        // give
        List<String> existingLinks = Arrays.asList(
                "https://example.com/1",
                "https://example.com/2",
                "https://example.com/3");
        when(newsRepository.findAllLinks()).thenReturn(existingLinks);

        List<String> linksToCheck = Arrays.asList(
                "https://example.com/1",  // 존재하는 링크
                "https://example.com/4",  // 새로운 링크
                "https://example.com/5"   // 새로운 링크
        );

        // when
        List<String> validLinks = newsService.isValidLinks(linksToCheck);

        // then
        assertEquals(2, validLinks.size(), "Valid links 크기가 2이 아닙니다");
        assertTrue(validLinks.contains("https://example.com/4"),
                "Valid links에 'https://example.com/4'가 포함되지 않았습니다");
        assertTrue(validLinks.contains("https://example.com/5"),
                "Valid links should contain 'https://example.com/5'가 포함되지 않았습니다");
    }

    @Test
    @DisplayName("convertDateTime 메서드 테스트")
    void convertDateTimeTest() {
        // give
        String testPmDate = "입력 2020-01-01 오후 01:01";
        String testAmDate = "입력 2020-01-01 오전 01:01";
        String testNoonDate = "입력 2020-01-01 오후 12:01";
        String testMidnighDate = "입력 2020-01-01 오전 12:01";

        // when
        LocalDateTime convertDatePm = newsService.convertDateTime(testPmDate);
        LocalDateTime convertDateAm = newsService.convertDateTime(testAmDate);
        LocalDateTime convertDateNoon = newsService.convertDateTime(testNoonDate);
        LocalDateTime convertDateMidnight = newsService.convertDateTime(testMidnighDate);

        // then
        assertTrue(convertDatePm.isEqual(LocalDateTime.of(2020, 01, 01, 13, 01)),
                "오후 시간이 올바르지 않습니다.");
        assertTrue(convertDateAm.isEqual(LocalDateTime.of(2020, 01, 01, 01, 01)),
                "오전 시간이 올바르지 않습니다.");
        assertTrue(convertDateNoon.isEqual(LocalDateTime.of(2020, 01, 01, 12, 01)),
                "오후 12시 시간이 올바르지 않습니다.");
        assertTrue(convertDateMidnight.isEqual(LocalDateTime.of(2020, 1, 1, 0, 1)),
                "오전 12시 시간이 올바르지 않습니다.");
    }

}
