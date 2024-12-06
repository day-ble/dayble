package com.example;

import com.example.application.NewsService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.List;

@SpringBootApplication
@EnableScheduling
public class Schedule {
    public static void main(String[] args) {
        SpringApplication.run(Schedule.class, args);
    }

    @Bean
    CommandLineRunner run(NewsService newsService) {
        return args -> {
            newsService.newsCrawling();
        };
    }
}
