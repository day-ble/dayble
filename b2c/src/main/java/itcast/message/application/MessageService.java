package itcast.message.application;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import net.nurigo.java_sdk.api.Message;
import net.nurigo.java_sdk.exceptions.CoolsmsException;

import itcast.exception.ErrorCodes;
import itcast.exception.ItCastApplicationException;
import itcast.message.dto.request.VerificationRequest;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MessageService {

    @Value("${sms.api.key}")
    private String apiKey;
    @Value("${sms.api.secret}")
    private String apiSecret;
    @Value("${sms.sender.phone}")
    private String fromNumber;
    private final RedisTemplate<String, Object> redisTemplate;
    private static final String VERIFICATION_CODE_PREFIX = "verification_code:";

    public String sendVerificationCode(String phoneNumber) {
        Message coolsms = new Message(apiKey, apiSecret);
        String randomNum = createRandomNumber();
        HashMap<String, String> params = makeParams(phoneNumber, randomNum);
        try {
            coolsms.send(params);
            redisTemplate.opsForValue().set(VERIFICATION_CODE_PREFIX + phoneNumber, randomNum, 5, TimeUnit.MINUTES);
        } catch (CoolsmsException e) {
            throw new ItCastApplicationException(ErrorCodes.VERIFICATION_CODE_SENDING_FAILED);
        }
        return randomNum;
    }

    private String createRandomNumber() {
        return String.format("%06d", (int)(Math.random() * 1000000));
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
            throw new ItCastApplicationException(ErrorCodes.VERIFICATION_CODE_MISMATCH);
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
        String storedCode = (String)redisTemplate.opsForValue()
                .get(VERIFICATION_CODE_PREFIX + requestDto.phoneNumber());
        if (storedCode != null) {
            storedCode = storedCode.replaceAll("\"", "");
        }
        return storedCode != null && storedCode.equals(requestDto.verificationCode());
    }
}