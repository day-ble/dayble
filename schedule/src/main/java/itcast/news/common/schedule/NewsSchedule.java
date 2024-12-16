package itcast.news.common.schedule;

import itcast.news.application.NewsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class NewsSchedule {

    private final NewsService newsService;

    @Scheduled(cron = "${scheduler.cron.crawling}")
    public void scheduleNewsCrawling() throws IOException {
        log.info("crawling....");
        newsService.newsCrawling();
        log.info("crawled Finish");
    }
}
