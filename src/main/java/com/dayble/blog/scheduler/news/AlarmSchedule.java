package com.dayble.blog.scheduler.news;

import com.dayble.blog.global.exception.DaybleApplicationException;
import com.dayble.blog.global.exception.ErrorCodes;
import com.dayble.blog.news.application.NewsService;
import com.dayble.blog.news.application.SendNewsService;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class AlarmSchedule {
    private final NewsService newsService;
    private final SendNewsService sendNewsService;

    @Scheduled(cron = "${scheduler.news.select-news}")
    public void selectNewsSchedule() {
        log.info("Selecting schedule....");
        LocalDate yesterday = LocalDate.now().minusDays(1);
        sendNewsService.selectNews(yesterday);
        log.info("Selecting schedule Finish");
    }

    @Scheduled(cron = "${scheduler.news.send-alarm}")
    public void sendEmailAlarmSchedule() {
        log.info("Sending email schedule....");
        sendNewsService.sendEmails();
        log.info("Sending email schedule Finish");
    }

    @Scheduled(cron = "${scheduler.news.send-alarm}")
    public void sendMessageAlarmSchedule() {
        log.info("Sending message schedule....");
        try {
            sendNewsService.sendMessages();
            log.info("Sending message schedule Finish");
        } catch (Exception exception) {
            throw new DaybleApplicationException(ErrorCodes.MESSAGE_SENDING_FAILED);
        }
    }
}

