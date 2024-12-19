package itcast.blog.parser;

import itcast.blog.client.JsoupCrawler;
import itcast.domain.blog.Blog;
import itcast.domain.blog.enums.BlogStatus;
import itcast.domain.blog.enums.Platform;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.nodes.Document;
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
public class VelogDataParser {

    private final JsoupCrawler jsoupCrawler;

    public List<String> getBlogUrls(final String jsonResponse) {
        final JSONObject jsonObject = new JSONObject(jsonResponse);
        final JSONArray trendingPosts = jsonObject.getJSONObject("data").getJSONArray("trendingPosts");

        return IntStream.range(0, trendingPosts.length())
                .mapToObj(trendingPosts::getJSONObject).map(post -> {
                    final String username = post.getJSONObject("user").getString("username");
                    final String urlSlug = post.getString("url_slug");
                    return "https://velog.io/@" + username + "/" + urlSlug; // BLOG URL
                }).toList();
    }

    public List<Blog> parseTrendingPosts(final List<String> blogUrl) {
        return blogUrl.stream()
                .map(url -> {
                    try {
                        final Document document = jsoupCrawler.getHtmlDocumentOrNull(url);

                        final String title = Objects.requireNonNull(document).title();
                        final String thumbnail = document.selectFirst("meta[property=og:image]").attr("content");
                        final String content = document.select("div[class^=sc-][class$=atom-one]").text();
                        final String publishedDate = document.select(".information > span:last-child").text();

                        log.info("title: {}", title);

                        LocalDate publishedAt = changePublishedDateType(publishedDate);
                        log.info("publishedAt: {}", publishedAt);

                        return Blog.builder()
                                .platform(Platform.VELOG)
                                .title(title)
                                .originalContent(content)
                                .publishedAt(publishedAt)
                                .link(url)
                                .thumbnail(thumbnail)
                                .status(BlogStatus.ORIGINAL)
                                .build();
                    } catch (Exception e) {
                        log.error("Error", e);
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .toList();
    }

    private LocalDate changePublishedDateType(String publishedDate) {
        LocalDateTime now = LocalDateTime.now();
        LocalDate today = now.toLocalDate();
        try {
            if (publishedDate.contains("분 전")) {
                int minutesAgo = Integer.parseInt(publishedDate.replaceAll("[^0-9]", ""));
                return now.minusMinutes(minutesAgo).toLocalDate();
            }

            if (publishedDate.contains("시간 전")) {
                int hoursAgo = Integer.parseInt(publishedDate.replaceAll("[^0-9]", ""));
                return now.minusHours(hoursAgo).toLocalDate();
            }

            if (publishedDate.contains("일 전")) {
                int daysAgo = Integer.parseInt(publishedDate.replaceAll("[^0-9]", ""));
                return today.minusDays(daysAgo);
            }

            if (publishedDate.contains("년")) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일");
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
