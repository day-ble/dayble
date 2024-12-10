package itcast.news.common;

import itcast.news.application.NewsService;
import itcast.news.common.schedule.NewsSchedule;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@RequiredArgsConstructor
@EnableBatchProcessing
public class ScheduleConfig {
    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;
    private final NewsService newsService;

    @Bean(name = "crawlNewsJob")
    public Job crawlNewsJob() {
        return new JobBuilder("crawlNewsJob", jobRepository)
                .start(fetchLatestNewsStep())
                .build();
    }

    @Bean
    public Step fetchLatestNewsStep() {
        return new StepBuilder("fetchLatestNewsStep", jobRepository)
                .tasklet((contribution, chunkContext) ->{
                    newsService.newsCrawling();
                    return RepeatStatus.FINISHED;
                },transactionManager)
                .build();
    }

    @Bean(name = "notificationsJob")
    public Job notificationsJob() {
        return new JobBuilder("notificationsJob",jobRepository)
                .start(notificationsStep())
                .build();
    }

    @Bean
    public Step notificationsStep() {
        return new StepBuilder("notificationsStep",jobRepository)
                .tasklet((contribution, chunkContext) -> {
                    return RepeatStatus.FINISHED;
                },transactionManager)
                .build();
    }

    @Bean(name = "deleteOldDataJob")
    public Job deleteOldDataJob() {
        return new JobBuilder("deleteOldDataJob", jobRepository)
                .start(removeDataStep())
                .build();
    }

    private Step removeDataStep() {
        return new StepBuilder("removeDataStep",jobRepository)
                .tasklet((contribution, chunkContext) -> {
                    return RepeatStatus.FINISHED;
                },transactionManager)
                .build();
    }

}
