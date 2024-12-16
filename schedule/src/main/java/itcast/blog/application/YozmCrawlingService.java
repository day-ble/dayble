package itcast.blog.application;

import itcast.blog.client.JsoupCrawler;
import itcast.blog.repository.BlogRepository;
import itcast.domain.blog.Blog;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.IntStream;

@Service
@Slf4j
@RequiredArgsConstructor
public class YozmCrawlingService {

    private static final int MAX_PAGE = 6;
    private static final String BASE_URL = "https://yozm.wishket.com/magazine/list/develop/?sort=new&page=";
    private static final String SORTED_URL = "&sort=new&q=";

    private final JsoupCrawler jsoupCrawler;
    private final BlogRepository blogRepository;

    public List<Blog> crawlBlogs(final int maxPage) {
        final String DEFAULT_PUBLISHED_AT = "2024-12-12T12:12:12";    // 출판일 해결 시 삭제

        final List<Blog> blogs = IntStream.range(1, maxPage)
                .mapToObj(page -> BASE_URL + page + SORTED_URL)
                .map(jsoupCrawler::getHtmlDocumentOrNull).filter(Objects::nonNull)
                .map(doc -> doc.select("a.item-title.link-text.link-underline.text900"))
                .flatMap(Elements::stream)
                .map(link -> link.attr("abs:href"))
                .map(href -> {
                    Document document = jsoupCrawler.getHtmlDocumentOrNull(href);
                    String title = Objects.requireNonNull(document).title();
                    String thumbnail = document.selectFirst("meta[property=og:image]").attr("content");
                    String content = document.select("div.next-news-contents").text();
                    String publishedDate = document.select("div.content-meta-elem").eq(5).text();

                    log.info("title: {}", title);
                    return Blog.createYozmBlog(title, content, DEFAULT_PUBLISHED_AT, href, thumbnail);
                })
                .toList();
        return blogs;
    }

    @Scheduled(cron = "${crawler.yozm.cron}")
    public void yozmCrawling() {
        log.info("Yozm Crawling Start ...");

        final List<Blog> blogs = crawlBlogs(MAX_PAGE);
        blogRepository.saveAll(blogs);

        log.info("Yozm Crawling & Save!");
    }
}
