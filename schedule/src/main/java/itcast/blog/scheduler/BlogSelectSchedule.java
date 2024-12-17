package itcast.blog.scheduler;

import itcast.blog.application.BlogSelectService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Slf4j(topic = "블로그 선택 스케쥴")
@Component
@RequiredArgsConstructor
public class BlogSelectSchedule {

    private final BlogSelectService blogSelectService;

    @Scheduled(cron = "${scheduler.blog.selecting}")
    public void selectForSend(){
        log.info("Blog Select Start ...");

        LocalDate today = LocalDate.now();
        blogSelectService.selectBlogs(today);

        log.info("Blog Select Finished!!");
    }
}
