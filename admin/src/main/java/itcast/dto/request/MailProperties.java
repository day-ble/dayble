package itcast.dto.request;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "spring.mail")
@Getter
@Setter
public class MailProperties {
    private String host;
    private int port;
    private String username;
    private String password;
    private SmtpProperties properties = new SmtpProperties();

    @Getter
    @Setter
    public static class SmtpProperties {
        private boolean auth;
        private boolean starttlsEnable;
        private boolean sslEnable;
    }
}
