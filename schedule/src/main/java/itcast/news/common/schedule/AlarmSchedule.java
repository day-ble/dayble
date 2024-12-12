package itcast.news.common.schedule;

import itcast.news.application.NewsService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import reactor.core.scheduler.Scheduler;

@Component
@RequiredArgsConstructor
public class AlarmSchedule {
    private final NewsService newsService;

    @Scheduled(cron = "${spring.scheduler.cron.alarm-Scheduled}")
    public void CreateAlarmSchedule() {
        System.out.println("crawling....");
        newsService.newsAlarm();
        System.out.println("crawled End");

    }
}
