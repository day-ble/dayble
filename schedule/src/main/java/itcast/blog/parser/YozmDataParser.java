package itcast.blog.parser;

import itcast.blog.client.JsoupCrawler;
import itcast.domain.blog.Blog;

import java.time.LocalDateTime;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.stream.IntStream;

@Slf4j
@Component
@RequiredArgsConstructor
public class YozmDataParser {

    private static final int MAX_PAGE = 6;
    private static final String BASE_URL = "https://yozm.wishket.com/magazine/list/develop/?sort=new&page=";
    private static final String SORTED_URL = "&sort=new&q=";

    private final JsoupCrawler jsoupCrawler;

    public List<String> getBlogUrls() {
        return IntStream.range(1, MAX_PAGE)
                .mapToObj(pageNum -> BASE_URL + pageNum + SORTED_URL)
                .map(jsoupCrawler::getHtmlDocumentOrNull).filter(Objects::nonNull)
                .map(doc -> doc.select("a.item-title.link-text.link-underline.text900"))
                .flatMap(Elements::stream)
                .map(link -> link.attr("abs:href"))
                .toList();
    }

    public List<Blog> parseTrendingPosts(List<String> blogUrls) {
        final LocalDateTime DEFAULT_PUBLISHED_AT = LocalDateTime.of(2024, 12, 12, 12, 12, 12);
        return blogUrls.stream()
                .map(url -> {
                    Document document = jsoupCrawler.getHtmlDocumentOrNull(url);
                    String title = Objects.requireNonNull(document).title();
                    String thumbnail = document.selectFirst("meta[property=og:image]").attr("content");
                    String content = document.select("div.next-news-contents").text();
                    String publishedDate = document.select("div.content-meta-elem").eq(5).text();

                    log.info("title: {}", title);
                    return Blog.createYozmBlog(title, content, DEFAULT_PUBLISHED_AT, url, thumbnail);
                })
                .filter(Objects::nonNull)
                .toList();
    }
}
