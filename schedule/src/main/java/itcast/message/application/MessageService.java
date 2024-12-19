package itcast.message.application;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import net.nurigo.sdk.NurigoApp;
import net.nurigo.sdk.message.exception.NurigoMessageNotReceivedException;
import net.nurigo.sdk.message.model.FailedMessage;
import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.model.StorageType;
import net.nurigo.sdk.message.service.DefaultMessageService;

import itcast.ResponseTemplate;
import itcast.exception.ErrorCodes;
import itcast.exception.ItCastApplicationException;
import itcast.message.dto.request.MessageContent;
import itcast.message.dto.request.RecieverPhoneNumber;
import itcast.message.dto.request.SendMessageRequest;
import jakarta.annotation.PostConstruct;

@Service
public class MessageService {

    private DefaultMessageService messageService;

    @Value("${sms.api.key}")
    private String apiKey;

    @Value("${sms.api.secret}")
    private String apiSecret;

    private String apiUrl = "https://api.coolsms.co.kr";

    public MessageService() {
        this.messageService = null;
    }

    @PostConstruct
    public void initialize() {
        this.messageService = NurigoApp.INSTANCE.initialize(apiKey, apiSecret, apiUrl);
    }

    public ResponseTemplate<List<FailedMessage>> sendMessages(SendMessageRequest request) {
        ArrayList<Message> messageList = new ArrayList<>();
        List<MessageContent> contentList = request.contentList();
        List<RecieverPhoneNumber> phoneNumbers = request.phoneNumbers();

        StringBuilder textBuilder = new StringBuilder();
        for (MessageContent content : contentList) {
            String title = "■ Today's Message";
            String contentTitle = ": " + content.title();
            String summary = "▶ <요약 내용> " + content.summary();
            String originalLink = "▶ <본문 보기> " + content.originalLink();

            textBuilder.append(title).append("\n")
                    .append(contentTitle).append("\n")
                    .append(summary).append("\n")
                    .append(originalLink).append("\n\n");
        }
        try {
            ClassPathResource resource = new ClassPathResource("static/images/image.jpg");
            File file = resource.getFile();
            String imageId = messageService.uploadFile(file, StorageType.MMS, null);

            Message message = new Message();
            message.setFrom("01033124811");
            List<String> phoneNumberList = phoneNumbers.stream()
                    .map(RecieverPhoneNumber::phoneNumber)
                    .collect(Collectors.toList());
            message.setTo(String.join(",", phoneNumberList));
            message.setText(textBuilder.toString());
            message.setSubject("[IT-Cast 뉴스레터]");
            message.setImageId(imageId);
            messageList.add(message);

            this.messageService.send(messageList, false, true);
            return new ResponseTemplate<>(HttpStatus.OK, "메세지가 발송되었습니다.");
        } catch (NurigoMessageNotReceivedException exception) {
            List<FailedMessage> failedMessages = exception.getFailedMessageList();
            return new ResponseTemplate<>(HttpStatus.BAD_REQUEST, "메시지 발송 실패", failedMessages);
        } catch (Exception exception) {
            throw new ItCastApplicationException(ErrorCodes.MESSAGE_SENDING_FAILED);
        }
    }
}