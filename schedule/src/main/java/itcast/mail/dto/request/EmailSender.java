package itcast.mail.dto.request;

import com.amazonaws.services.simpleemail.model.Body;
import com.amazonaws.services.simpleemail.model.Content;
import com.amazonaws.services.simpleemail.model.Destination;
import com.amazonaws.services.simpleemail.model.Message;
import com.amazonaws.services.simpleemail.model.SendEmailRequest;
import java.io.IOException;
import java.util.Base64;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Getter
@Component
@RequiredArgsConstructor
public class EmailSender {

    @Value("${aws.ses.sender-email}")
    private String senderEmail;

    private final TemplateEngine templateEngine;

    public SendEmailRequest from(final SendMailRequest request) {
        final Destination destination = new Destination()
                .withToAddresses(request.receivers());

        final Message message = new Message()
                .withSubject(createContent(String.format("%s - %s", senderEmail, request.subject())))
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
        context.setVariable("subject", request.subject());
        context.setVariable("content", request.content());

        try {
            final String base64Image = getBase64EncodedImage("/static/images/logo.png");
            context.setVariable("logoImage", base64Image);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e);
        }

        return templateEngine.process("email-template", context);
    }

    private String getBase64EncodedImage(final String imagePath) throws IOException {
        final Resource resource = new ClassPathResource(imagePath);
        byte[] bytes = StreamUtils.copyToByteArray(resource.getInputStream());
        return Base64.getEncoder().encodeToString(bytes);
    }
}
