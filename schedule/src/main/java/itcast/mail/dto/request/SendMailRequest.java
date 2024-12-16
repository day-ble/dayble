package itcast.mail.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import java.util.List;

public record SendMailRequest(
        @Email
        List<String> receivers,
        @NotBlank
        String subject,
        @NotBlank
        String content
) {
}
