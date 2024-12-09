package itcast.news.application;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class NewsService {

    @Value("${spring.crawler.naver-it-url}")
    private String naverUrl;

    public List<String> newsCrawling() throws IOException {
        Document document = Jsoup.connect(naverUrl).get();
        Elements articles = document.select(".sa_thumb_inner");

        List<String> links = new ArrayList<>();
        articles.forEach(article -> {
            String link = article.select("a").attr("href");
            links.add(link);
        });

        links.forEach(link -> {

            try {
                Document url = Jsoup.connect(link).get();
                String titles = url.select("#title_area").text();
                String content = url.select("#dic_area").text();
                String date =
                        url.select(".media_end_head_info_datestamp_bunch").text();
                String thumbnail =
                        url.selectFirst("meta[property=og:image]").attr("content");

                // 저장 부분
                System.out.println(link);
                System.out.println(titles);
                System.out.println(content);
                System.out.println(date);
                if (thumbnail.isEmpty()){
                    System.out.println("썸네일이 없습니다");
                }
                System.out.println(thumbnail);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        return links;
    }
}