package itcast.news.common.schedule;

import itcast.news.application.NewsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class AlarmSchedule {
    private final NewsService newsService;

    @Scheduled(cron = "${scheduler.cron.alarm-scheduled}")
    public void CreateAlarmSchedule() {
        log.info("alarm schedule....");
        newsService.newsAlarm();
        log.info("alarm schedule Finish");
    }
}
