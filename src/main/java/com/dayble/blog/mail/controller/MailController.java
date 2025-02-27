package com.dayble.blog.mail.controller;

import static com.dayble.blog.global.exception.ErrorCodes.MAIL_AUTH_CODE_EXPIRED;
import static com.dayble.blog.global.exception.ErrorCodes.MAIL_AUTH_CODE_MISMATCH;

import com.dayble.blog.global.ResponseTemplate;
import com.dayble.blog.global.exception.DaybleApplicationException;
import com.dayble.blog.global.interceptor.CheckAuth;
import com.dayble.blog.mail.application.MailService;
import com.dayble.blog.mail.controller.request.EmailRequest;
import com.dayble.blog.mail.controller.request.SendValidateMailRequest;
import com.dayble.blog.mail.controller.request.VerifyMailRequest;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/mail")
@RequiredArgsConstructor
public class MailController {

    private final MailService mailService;
    private final RedisTemplate<String, Object> redisTemplate;

    @CheckAuth
    @PostMapping()
    public ResponseTemplate<Void> sendEmailAuthenticationCode(@RequestBody final EmailRequest request) {
        final String code = UUID.randomUUID().toString().substring(0, 6);

        redisTemplate.opsForValue().set("email-verification:" + request.email(), code, 5, TimeUnit.MINUTES);

        final SendValidateMailRequest mailRequest = SendValidateMailRequest.of(request.email(), code);
        mailService.sendValidateEmail(mailRequest);

        return new ResponseTemplate<>(HttpStatus.OK, "인증 메일이 발송되었습니다.", null);
    }

    @CheckAuth
    @PostMapping("/verify")
    public ResponseTemplate<Void> verifyEmailAuthenticationCode(
            @RequestBody final VerifyMailRequest request
    ) {
        final String code = (String) redisTemplate.opsForValue().get("email-verification:" + request.email());

        if (code.isEmpty()) {
            throw new DaybleApplicationException(MAIL_AUTH_CODE_EXPIRED);
        }

        if (!request.authenticationCode().equals(code)) {
            throw new DaybleApplicationException(MAIL_AUTH_CODE_MISMATCH);
        }
        redisTemplate.opsForValue().set(
                "VERIFIED_EMAIL" + request.email(),
                true,
                5,
                TimeUnit.MINUTES
        );
        redisTemplate.delete("email-verification:" + request.email());

        return new ResponseTemplate<>(HttpStatus.OK, "이메일 인증이 완료되었습니다.");
    }
}
