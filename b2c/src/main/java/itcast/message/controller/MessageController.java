package itcast.message.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import itcast.ResponseTemplate;
import itcast.jwt.CheckAuth;
import itcast.message.application.MessageService;
import itcast.message.dto.request.VerificationRequest;
import itcast.message.dto.response.VericationResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/code")
public class MessageController {

    private final MessageService messageService;

    @CheckAuth
    @PostMapping("/send")
    public ResponseTemplate<String> sendVerificationCode(@RequestBody VerificationRequest verificationRequest) {
        String phoneNumber = verificationRequest.phoneNumber();
        String verificationCode = messageService.sendVerificationCode(phoneNumber);
        return new ResponseTemplate<>(HttpStatus.OK, "인증번호 전송이 완료되었습니다.", verificationCode);
    }

    @CheckAuth
    @PostMapping("/verify")
    public ResponseTemplate<Void> verifyVerificationCode(@RequestBody VerificationRequest verificationRequest) {
        messageService.verifyVerificationCode(verificationRequest);
        return new ResponseTemplate<>(HttpStatus.OK, "인증이 완료되었습니다.");
    }
}