package itcast.blog.controller;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class YozmJsoupCrawler {

    public Document getHtmlDocument(String pageUrl) throws IOException {
        return Jsoup.connect(pageUrl).get();
    }
}
