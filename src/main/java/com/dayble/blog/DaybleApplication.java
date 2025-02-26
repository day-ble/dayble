package com.dayble.blog;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@EnableAsync
@SpringBootApplication
public class DaybleApplication {
    public static void main(String[] args) {
        SpringApplication.run(DaybleApplication.class, args);
    }
}


