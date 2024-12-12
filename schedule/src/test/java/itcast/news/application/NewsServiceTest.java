package itcast.news.application;

import itcast.news.repository.NewsRepository;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;


@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class NewsServiceTest {

    @Autowired
    private NewsService newsService;

    @Mock
    private NewsRepository newsRepository;

    @Test
    @DisplayName("크롤링 성공 테스트")
    void crawlingSuccessTest() throws IOException {
        // give
        String url = "https://news.naver.com/breakingnews/section/105/283";
        Document document = Jsoup.connect(url).get();

        // when
        Elements articles = document.select(".sa_thumb_inner");

        // then
        assertFalse(articles.isEmpty(), "Articles should not be empty");
    }

    @Test
    @DisplayName("링크 크롤링 성공 테스트")
    void linkCrawlingSuccessTest() throws IOException {
        // give
        String url = "https://news.naver.com/breakingnews/section/105/283";
        Document document = Jsoup.connect(url).get();
        List<String> links = new ArrayList<>();
        // when
        // then
    }
}
