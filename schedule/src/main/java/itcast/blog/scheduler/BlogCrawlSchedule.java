package itcast.blog.scheduler;

import itcast.blog.application.BlogCrawlService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j(topic = "블로그 크롤링 스케쥴")
@Component
@RequiredArgsConstructor
public class BlogCrawlSchedule {

    private final BlogCrawlService blogCrawlService;

    @Scheduled(cron = "${scheduler.blog.crawling}")
    public void velogCrawling() {
        log.info("Velog Crawling Start ...");

        blogCrawlService.crawlVelog();

        log.info("Velog Crawling & Save!");
    }

    @Scheduled(cron = "${scheduler.blog.crawling}")
    public void yozmCrawling() {
        log.info("Yozm Crawling Start ...");

        blogCrawlService.crawlYozm();

        log.info("Yozm Crawling & Save!");
    }
}
