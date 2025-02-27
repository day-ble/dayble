package com.dayble.blog.message.controller;

import com.dayble.blog.global.ResponseTemplate;
import com.dayble.blog.global.interceptor.CheckAuth;
import com.dayble.blog.message.application.MessageService;
import com.dayble.blog.message.controller.dto.request.VerificationRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
