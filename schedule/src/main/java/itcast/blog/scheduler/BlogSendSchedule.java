package itcast.blog.scheduler;

import itcast.blog.application.BlogSendService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Slf4j(topic = "블로그 전송 스케쥴")
@Component
@RequiredArgsConstructor
public class BlogSendSchedule {

    private final BlogSendService blogSendService;

    @Scheduled(cron = "${scheduler.blog.sending}")
    public void sendEmail(){
        log.info("Blog Send Start ...");
        
        LocalDate today = LocalDate.now();
        try {
            blogSendService.sendBlogForEmail(today);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        log.info("Blog Send Finished !!");
    }

/*    @Scheduled
    public void sendKakaoTalk(){
        log.info("Blog Send Start ...");
        log.info("Blog Send Finished !!");
    }*/
}
