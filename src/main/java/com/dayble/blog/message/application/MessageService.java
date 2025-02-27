package com.dayble.blog.message.application;

import com.dayble.blog.global.exception.DaybleApplicationException;
import com.dayble.blog.global.exception.ErrorCodes;
import com.dayble.blog.message.controller.dto.request.MessageContent;
import com.dayble.blog.message.controller.dto.request.RecieverPhoneNumber;
import com.dayble.blog.message.controller.dto.request.SendMessageRequest;
import com.dayble.blog.message.controller.dto.request.VerificationRequest;
import jakarta.annotation.PostConstruct;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.nurigo.java_sdk.exceptions.CoolsmsException;
import net.nurigo.sdk.NurigoApp;
import net.nurigo.sdk.message.exception.NurigoMessageNotReceivedException;
import net.nurigo.sdk.message.model.FailedMessage;
import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.model.StorageType;
import net.nurigo.sdk.message.service.DefaultMessageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class MessageService {

    private static final String VERIFICATION_CODE_PREFIX = "verification_code:";

    private final RedisTemplate<String, Object> redisTemplate;

    private DefaultMessageService messageService;

    @Value("${sms.api.key}")
    private String apiKey;

    @Value("${sms.api.secret}")
    private String apiSecret;

    @Value("${sms.sender.phone}")
    private String fromNumber;

    private String apiUrl = "https://api.coolsms.co.kr";

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

    public String sendVerificationCode(String phoneNumber) {
        net.nurigo.java_sdk.api.Message coolsms = new net.nurigo.java_sdk.api.Message(apiKey, apiSecret);
        String randomNum = createRandomNumber();
        HashMap<String, String> params = makeParams(phoneNumber, randomNum);
        try {
            coolsms.send(params);
            redisTemplate.opsForValue().set(VERIFICATION_CODE_PREFIX + phoneNumber, randomNum, 5, TimeUnit.MINUTES);
        } catch (CoolsmsException e) {
            throw new DaybleApplicationException(ErrorCodes.VERIFICATION_CODE_SENDING_FAILED);
        }
        return randomNum;
    }

    private String createRandomNumber() {
        return String.format("%06d", (int) (Math.random() * 1000000));
    }

    private HashMap<String, String> makeParams(String to, String randomNum) {
        HashMap<String, String> params = new HashMap<>();
        params.put("from", fromNumber);
        params.put("type", "SMS");
        params.put("to", to);
        params.put("text", "인증번호는 " + randomNum + " 입니다.");
        return params;
    }

    public void verifyVerificationCode(VerificationRequest requestDto) {
        if (!isVerify(requestDto)) {
            throw new DaybleApplicationException(ErrorCodes.VERIFICATION_CODE_MISMATCH);
        }
        redisTemplate.opsForValue().set(
                "VERIFIED_PHONE_NUMBER" + requestDto.phoneNumber(),
                true,
                5,
                TimeUnit.MINUTES
        );
        redisTemplate.delete(VERIFICATION_CODE_PREFIX + requestDto.phoneNumber());
    }

    private boolean isVerify(VerificationRequest requestDto) {
        String storedCode = (String) redisTemplate.opsForValue()
                .get(VERIFICATION_CODE_PREFIX + requestDto.phoneNumber());
        if (storedCode != null) {
            storedCode = storedCode.replaceAll("\"", "");
        }
        return storedCode != null && storedCode.equals(requestDto.verificationCode());
    }
}
