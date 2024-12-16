package itcast.blog.parser;

import itcast.blog.client.JsoupCrawler;
import itcast.domain.blog.Blog;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.nodes.Document;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.IntStream;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class VelogDataParser {

    private final JsoupCrawler jsoupCrawler;

    public List<String> getBlogUrls(final String jsonResponse) {
        final JSONObject jsonObject = new JSONObject(jsonResponse);
        final JSONArray trendingPosts = jsonObject.getJSONObject("data").getJSONArray("trendingPosts");

        return IntStream.range(0, trendingPosts.length())
                .mapToObj(trendingPosts::getJSONObject)
                .map(post -> {
                    final String username = post.getJSONObject("user").getString("username");
                    final String urlSlug = post.getString("url_slug");
                    return "https://velog.io/@" + username + "/" + urlSlug; // BLOG URL
                })
                .toList();
    }

    public List<Blog> parseTrendingPosts(final List<String> blogUrl) {
        final String DEFAULT_PUBLISHED_AT = "2024-12-12T12:12:12";    // 출판일 해결 시 삭제

        return blogUrl.stream()
                .map(url -> {
                    try {
                        final Document document = jsoupCrawler.getHtmlDocumentOrNull(url);

                        final String title = Objects.requireNonNull(document).title();
                        final String thumbnail = document.selectFirst("meta[property=og:image]").attr("content");
                        final String content = document.select("div.sc-eGRUor.gdnhbG.atom-one").text();
                        final String publishedAt = document.select(".information").eq(3).text();

                        log.info("title: {}", title);

                        return Blog.createVelogBlog(url, title, thumbnail, content, DEFAULT_PUBLISHED_AT);
                    } catch (Exception e) {
                        log.error("Error", e);
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .toList();
    }
}
