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
public class OldDataSchedule {
    private final NewsService newsService;

    @Scheduled(cron = "${spring.scheduler.cron.old-delete-data}")
    public void scheduleNewsCrawling() throws IOException {
        log.info("deleting old data....");
        newsService.deleteOldData();
        log.info("deleting old data....");
    }
}
