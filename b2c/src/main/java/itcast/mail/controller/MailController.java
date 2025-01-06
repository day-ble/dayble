package itcast.mail.controller;

import static itcast.exception.ErrorCodes.MAIL_AUTH_CODE_EXPIRED;
import static itcast.exception.ErrorCodes.MAIL_AUTH_CODE_MISMATCH;

import itcast.ResponseTemplate;
import itcast.exception.ItCastApplicationException;
import itcast.jwt.CheckAuth;
import itcast.mail.application.MailService;
import itcast.mail.dto.request.EmailRequest;
import itcast.mail.dto.request.SendValidateMailRequest;
import itcast.mail.dto.request.VerifyMailRequest;
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
            throw new ItCastApplicationException(MAIL_AUTH_CODE_EXPIRED);
        }

        if (!request.authenticationCode().equals(code)) {
            throw new ItCastApplicationException(MAIL_AUTH_CODE_MISMATCH);
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
