package itcast.news.common.schedule;

import itcast.news.application.NewsService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class OldDataSchedule {
    private final NewsService newsService;

    @Scheduled(cron = "${spring.scheduler.cron.old-delete-data}")
    public void scheduleNewsCrawling() throws IOException {
        System.out.println("deleting old data....");
        newsService.deleteOldData();
        System.out.println("deleting old data Finish");
    }
}
