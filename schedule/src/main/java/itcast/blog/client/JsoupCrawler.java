package itcast.blog.client;

import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Configuration
@Slf4j
public class JsoupCrawler {

    public Document getHtmlDocument(final String pageUrl) throws IOException {
        return Jsoup.connect(pageUrl).get();
    }

    public Document getHtmlDocumentOrNull(final String url) {
        try {
            return getHtmlDocument(url);
        } catch (IOException e) {
            log.error("Document Parse Error", e);
            return null;
        }
    }
}
