package itcast.message.application;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;

import net.nurigo.sdk.NurigoApp;
import net.nurigo.sdk.message.exception.NurigoMessageNotReceivedException;
import net.nurigo.sdk.message.model.FailedMessage;
import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.model.StorageType;
import net.nurigo.sdk.message.service.DefaultMessageService;
import itcast.message.dto.request.MessageContent;
import itcast.message.dto.request.RecieverPhoneNumber;
import itcast.message.dto.request.SendMessageRequest;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class MessageService {

    private DefaultMessageService messageService;

    @Value("${sms.api.key}")
    private String apiKey;

    @Value("${sms.api.secret}")
    private String apiSecret;

    @Value("${sms.sender.phone}")
    private String fromNumber;

    private String apiUrl = "https://api.coolsms.co.kr";

    public MessageService() {
        this.messageService = null;
    }

    @PostConstruct
    public void initialize() {
        this.messageService = NurigoApp.INSTANCE.initialize(apiKey, apiSecret, apiUrl);
    }

    @Async
    public void sendMessages(SendMessageRequest request) {
        ArrayList<Message> messageList = new ArrayList<>();
        List<MessageContent> contentList = request.contentList();
        List<RecieverPhoneNumber> phoneNumbers = request.phoneNumbers();

        StringBuilder textBuilder = new StringBuilder();
        for (MessageContent content : contentList) {
            String title = "■ Today's Message";
            String contentTitle = content.title();
            String summaryTitle = "▶ <요약 내용>";
            String summaryContent = content.content();
            String originalLinkTitle = "▶ <본문 보기>";
            String originalLinkContent = content.link();

            textBuilder.append(title).append("\n")
                    .append(contentTitle).append("\n\n")
                    .append(summaryTitle).append("\n")
                    .append(summaryContent).append("\n\n")
                    .append(originalLinkTitle).append("\n")
                    .append(originalLinkContent).append("\n\n");
        }
        try {
            ClassPathResource resource = new ClassPathResource("static/images/image.jpg");
            File file = resource.getFile();
            String imageId = messageService.uploadFile(file, StorageType.MMS, null);

            Message message = new Message();
            message.setFrom(fromNumber);
            List<String> phoneNumberList = phoneNumbers.stream()
                    .map(RecieverPhoneNumber::phoneNumber)
                    .collect(Collectors.toList());
            message.setTo(String.join(",", phoneNumberList));
            message.setText(textBuilder.toString());
            message.setSubject("[IT-Cast 뉴스레터]");
            message.setImageId(imageId);
            messageList.add(message);

            this.messageService.send(messageList, false, true);
        } catch (NurigoMessageNotReceivedException exception) {
            List<FailedMessage> failedMessages = exception.getFailedMessageList();
            log.error("메시지 발송 실패. 실패한 메시지 목록: {}", failedMessages);
        } catch (Exception exception) {
            log.error("메시지 전송 중 예기치 않은 오류 발생: {}", exception.getMessage(), exception);
        }
    }
}