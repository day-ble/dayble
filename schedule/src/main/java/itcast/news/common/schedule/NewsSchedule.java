package itcast.news.common.schedule;

import itcast.news.application.NewsService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class NewsSchedule {
    private NewsService newsService;

    @Scheduled(cron = "${spring.scheduler.cron.crawl-every-3-hour}")
    public void scheduleNewsCrawling() throws IOException {
        System.out.println("crawling....");
        newsService.newsCrawling();
        System.out.println("crawling finished.");
    }
}
