package itcast.message.application;


import static org.mockito.Mockito.*;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.junit.jupiter.api.Test;

import net.nurigo.java_sdk.api.Message;
import itcast.message.dto.request.VerificationRequest;

@ExtendWith(MockitoExtension.class)
class MessageServiceTest {

    @Mock
    private RedisTemplate<String, Object> redisTemplate;

    @Mock
    private ValueOperations<String, Object> valueOperations;

    @Mock
    private Message coolsms;

    @InjectMocks
    private MessageService messageService;

    @Test
    @DisplayName("사용자가 입력한 인증 번호와 redis에 저장된 번호를 비교해 일치할 경우 성공한다.")
    void verifyVerificationCode() {
        // Given
        String phoneNumber = "01012345678";
        String randomCode = "123456";

        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        when(valueOperations.get("verification_code:" + phoneNumber)).thenReturn(randomCode);

        VerificationRequest request = new VerificationRequest(phoneNumber, randomCode);

        // When
        messageService.verifyVerificationCode(request);

        // Then
        verify(valueOperations, times(1)).set(
                "VERIFIED_" + phoneNumber, true, 5, TimeUnit.MINUTES
        );
        verify(redisTemplate, times(1)).delete("verification_code:" + phoneNumber);
    }
}
