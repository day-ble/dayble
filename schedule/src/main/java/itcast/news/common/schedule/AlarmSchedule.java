package itcast.news.common.schedule;

import itcast.news.application.NewsService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AlarmSchedule {
    private final NewsService newsService;

    @Scheduled(cron = "${spring.scheduler.cron.alarm-Scheduled}")
    public void createAlarmSchedule() {
        System.out.println("alarm schedule....");
        newsService.newsAlarm();
        System.out.println("alarm schedule Finish");
    }
}
