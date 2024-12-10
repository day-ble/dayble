package itcast.news.common.schedule;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;


@Component
@RequiredArgsConstructor
public class NewsSchedule {
    @Qualifier("crawlNewsJob")
    private final Job crawlingJob;
    private final JobLauncher jobLauncher;

    @Scheduled(cron = "${spring.scheduler.cron.crawl-every-3-hour}")
    public void scheduleNewsCrawling() throws
            JobInstanceAlreadyCompleteException,
            JobExecutionAlreadyRunningException,
            JobParametersInvalidException,
            JobRestartException {
        System.out.println("crawling....");
        jobLauncher.run(crawlingJob,new JobParametersBuilder()
                .addDate("date", new Date())
                .toJobParameters());
    }
}