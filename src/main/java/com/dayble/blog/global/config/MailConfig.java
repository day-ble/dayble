package com.dayble.blog.global.config;

import com.dayble.blog.admin.controller.dto.request.MailProperties;
import java.util.Properties;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

@Configuration
@RequiredArgsConstructor
public class MailConfig {

    private final MailProperties mailProperties;

    @Bean
    public JavaMailSender javaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(mailProperties.getHost());
        mailSender.setPort(mailProperties.getPort());
        mailSender.setUsername(mailProperties.getUsername());
        mailSender.setPassword(mailProperties.getPassword());

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", String.valueOf(mailProperties.getProperties().isAuth()));
        props.put("mail.smtp.ssl.enable", String.valueOf(mailProperties.getProperties().isSslEnable()));
        props.put("mail.smtp.starttls.enable", String.valueOf(mailProperties.getProperties().isStarttlsEnable()));
        props.put("mail.debug", "true");

        return mailSender;
    }
}
