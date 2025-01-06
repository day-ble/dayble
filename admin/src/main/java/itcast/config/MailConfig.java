package itcast.config;

import itcast.dto.request.MailProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import java.util.Properties;

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
