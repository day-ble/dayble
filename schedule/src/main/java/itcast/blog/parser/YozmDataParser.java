package itcast.blog.parser;

import itcast.blog.client.JsoupCrawler;
import itcast.domain.blog.Blog;
import itcast.domain.blog.enums.BlogStatus;
import itcast.domain.blog.enums.Platform;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
        return blogUrls.stream()
                .map(url -> {
                    Document document = jsoupCrawler.getHtmlDocumentOrNull(url);
                    String title = Objects.requireNonNull(document).title();
                    String thumbnail = document.selectFirst("meta[property=og:image]").attr("content");
                    String content = document.select("div.next-news-contents").text();
                    String publishedDate = document.select("div.content-meta-elem").eq(5).text();

                    LocalDate publishedAt = changePublishedDateType(publishedDate);

                    log.info("title: {}", title);

                    return Blog.builder()
                            .platform(Platform.YOZM)
                            .title(title)
                            .originalContent(content)
                            .publishedAt(publishedAt)
                            .link(url)
                            .thumbnail(thumbnail)
                            .status(BlogStatus.ORIGINAL)
                            .build();
                })
                .filter(Objects::nonNull)
                .toList();
    }

    private LocalDate changePublishedDateType(String publishedDate) {
        LocalDateTime now = LocalDateTime.now();
        LocalDate today = now.toLocalDate();
        try {

            if (publishedDate.contains("시간 전")) {
                int hoursAgo = Integer.parseInt(publishedDate.replaceAll("[^0-9]", ""));
                return now.minusHours(hoursAgo).toLocalDate();
            }

            if (publishedDate.contains("일 전")) {
                int daysAgo = Integer.parseInt(publishedDate.replaceAll("[^0-9]", ""));
                return today.minusDays(daysAgo);
            }

            if (publishedDate.contains(".")) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd.");
                return LocalDate.parse(publishedDate, formatter);

            }
        } catch (Exception e) {
            log.error("Error Parsing PublishedDate: {}, exception", publishedDate, e);
            return today;
        }
        log.error("Invalid publishedDate format: {}", publishedDate);
        return today;
    }
}
