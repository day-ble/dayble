package itcast.mail.dto.request;

import com.amazonaws.services.simpleemail.model.Body;
import com.amazonaws.services.simpleemail.model.Content;
import com.amazonaws.services.simpleemail.model.Destination;
import com.amazonaws.services.simpleemail.model.Message;
import com.amazonaws.services.simpleemail.model.SendEmailRequest;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Getter
@Component
@RequiredArgsConstructor
public class EmailSender {

    private static final String MAIL_SUBJECT = "[IT-Cast 뉴스레터] 오늘의 인기 글을 확인해보세요~🔖";

    @Value("${aws.ses.sender-email}")
    private String senderEmail;

    private final TemplateEngine templateEngine;

    public SendEmailRequest from(final SendMailRequest request) {
        final Destination destination = new Destination()
                .withToAddresses(request.receivers());

        final Message message = new Message()
                .withSubject(createContent(String.format(MAIL_SUBJECT)))
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

    private String createHtmlBody(final SendMailRequest request) {
        final Context context = new Context();
        context.setVariable("sender", senderEmail);
        context.setVariable("subject", MAIL_SUBJECT);
        context.setVariable("contents", request.contents());

        return templateEngine.process("email-template", context);
    }
}
