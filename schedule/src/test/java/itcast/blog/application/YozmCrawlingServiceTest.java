/*
package itcast.blog.application;

import itcast.blog.client.JsoupCrawler;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.File;
import java.io.IOException;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class YozmCrawlingServiceTest {

    @InjectMocks
    private BlogCrawlService blogCrawlService;
    @Mock
    private JsoupCrawler yozmJsoupCrawler;

    @Test
    @DisplayName("각 페이지별로 블로그 가져오기")
    void crawlingSuccess() throws IOException {
        //given
        String url = "https://yozm.wishket.com/magazine/list/develop/?sort=new&page=1&sort=new&q=";
        String href = "https://yozm.wishket.com/magazine/detail/2888/";

        File inputFile = new File("src/test/java/itcast/blog/application/html/yozmExampleFile.html");
        Document document = Jsoup.parse(inputFile, "UTF-8");
        System.out.println(document);

        File detailFile = new File("src/test/java/itcast/blog/application/html/yozmDetailFile.html");
        Document document2 = Jsoup.parse(detailFile, "UTF-8");
        System.out.println(document2);

        when(yozmJsoupCrawler.getHtmlDocument(url)).thenReturn(document);
        when(yozmJsoupCrawler.getHtmlDocument(href)).thenReturn(document2);

        //when
*/
/*        List<Blog> blogs = yozmCrawlingService.crawlBlogs();

        //then
        assertThat(blogs).isNotNull();
        assertThat(blogs).hasSize(2);

        Blog blog = blogs.get(0);
        assertThat(blog.getLink()).isEqualTo("https://yozm.wishket.com/magazine/detail/2888/");
        assertThat(blog.getTitle()).isEqualTo("더 이상 외면할 수 없는 양자컴퓨터 | 요즘IT");
        assertThat(blog.getThumbnail()).isEqualTo("https://yozm.wishket.com/media/news/2888/3.png");*//*

    }
}
*/
