//package itcast.mail.application;
//
//import static org.mockito.Mockito.mock;
//import static org.mockito.Mockito.verify;
//import static org.mockito.Mockito.when;
//
//import itcast.mail.sender.EmailSender;
//import itcast.mail.dto.request.MailContent;
//import itcast.mail.dto.request.SendMailRequest;
//import java.util.List;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//@ExtendWith(MockitoExtension.class)
//class MailServiceTest {
//
//    @InjectMocks
//    private MailService mailService;
//
//    @Mock
//    private AmazonSimpleEmailService amazonSimpleEmailService;
//
//    @Mock
//    private EmailSender emailSender;
//
//    @Test
//    @DisplayName("올바르게 요청을 하면 메일이 전송된다.")
//    void mail_send_success_test() {
//        // given
//        final List<MailContent> contents = List.of(
//                new MailContent("블로그 제목 1", "요약 내용 1", "https://link1.com", "test thumbnail"),
//                new MailContent("블로그 제목 2", "요약 내용 2", "https://link2.com", "test thumbnail"),
//                new MailContent("블로그 제목 3", "요약 내용 3", "https://link3.com", "test thumbnail")
//        );
//
//        final SendMailRequest sendMailRequest = new SendMailRequest(
//                List.of("seonjun0906@gmail.com"),
//                contents
//        );
//
//        final SendEmailRequest sendEmailRequest = mock(SendEmailRequest.class);
//        when(emailSender.from(sendMailRequest)).thenReturn(sendEmailRequest);
//
//        // when
//        mailService.send(sendMailRequest);
//
//        // then
//        verify(emailSender).from(sendMailRequest);
//        verify(amazonSimpleEmailService).sendEmail(sendEmailRequest);
//    }
//}
