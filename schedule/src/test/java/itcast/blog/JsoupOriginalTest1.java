package itcast.blog;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class JsoupOriginalTest1 {
    public static void main(String[] args) {
        final String YOZM_URL = "https://yozm.wishket.com/magazine/list/develop/?sort=new";
        try {
            // 크롤링 해올 웹페이지 HTML 긁어오기
            Document originalDoc = Jsoup.connect(YOZM_URL).get();

            // 각각의 글 제목과 함께 있는 href 긁어오기
            Elements links = originalDoc.select("a.item-title.link-text.link-underline.text900");
            for (Element link : links) {
                String title = link.text();
                String href = link.attr("abs:href");    // link.absUrl("href")와 같음
                // title, href 출력
                System.out.println("title: " + title + "\n" + "href: " + href);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

/***
 * input placeholder 내용 가져오기
 * String html = "<html><head><title>First parse</title></head>"
 * Document doc = Jsoup.parse(html);
 * String title = doc.title();
 * String fullHtml = doc.outerHtml();
 *
 */
