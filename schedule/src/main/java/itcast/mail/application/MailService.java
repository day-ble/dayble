package itcast.mail.application;

import com.amazonaws.AmazonClientException;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.model.SendEmailRequest;
import itcast.mail.dto.request.EmailSender;
import itcast.mail.dto.request.SendMailRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class MailService {

    private final AmazonSimpleEmailService amazonSimpleEmailService;
    private final EmailSender emailSender;

    @Async("taskExecutor")
    public void send(final SendMailRequest sendMailRequest) {
        try {
            final SendEmailRequest emailRequest = emailSender.from(sendMailRequest);
            amazonSimpleEmailService.sendEmail(emailRequest);
        } catch (Exception ex) {
            throw new AmazonClientException(ex.getMessage(), ex);
        }
    }
}
