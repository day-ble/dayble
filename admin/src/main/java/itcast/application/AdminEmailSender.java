package itcast.application;

import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.model.Body;
import com.amazonaws.services.simpleemail.model.Content;
import com.amazonaws.services.simpleemail.model.Destination;
import com.amazonaws.services.simpleemail.model.Message;
import com.amazonaws.services.simpleemail.model.SendEmailRequest;
import itcast.dto.request.AdminSendMailRequest;
import itcast.mail.dto.request.SendMailRequest;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Getter
@Component
@RequiredArgsConstructor
public class AdminEmailSender {

    private static final String MAIL_SUBJECT = "[IT-Cast 뉴스레터] 오늘의 인기 글을 확인해보세요~🔖";
    private final AmazonSimpleEmailService amazonSimpleEmailService;

    @Value("${aws.ses.sender-email}")
    private String senderEmail;

    private final TemplateEngine templateEngine;

    public void send(AdminSendMailRequest request) {
        final SendEmailRequest emailRequest = from(request, request.getReceiver());
        amazonSimpleEmailService.sendEmail(emailRequest);
    }

    private SendEmailRequest from(final AdminSendMailRequest request, final String receiver) {
        final Destination destination = new Destination()
                .withToAddresses(receiver);

        final Message message = new Message()
                .withSubject(createContent(MAIL_SUBJECT))
                .withBody(new Body()
                        .withHtml(createContent(createHtmlBody(request))));

        return new SendEmailRequest()
                .withSource(senderEmail)
                .withDestination(destination)
                .withMessage(message);
    }

    private Content createContent(final String text) {
        return new Content()
                .withCharset("UTF-8")
                .withData(text);
    }

    private String createHtmlBody(final AdminSendMailRequest request) {
        final Context context = new Context();
        context.setVariable("sender", senderEmail);
        context.setVariable("subject", MAIL_SUBJECT);
        context.setVariable("contents", request.getContents());

        return templateEngine.process("email-template", context);
    }
}