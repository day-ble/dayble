package itcast.mail.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

import java.util.List;

@Builder
public record SendMailRequest(
        @Email
        List<String> receivers,
        @NotBlank
        List<MailContent> contents
) {
        public static SendMailRequest of(List<String> receivers, List<MailContent> contents) {
                return SendMailRequest.builder()
                        .receivers(receivers)
                        .contents(contents)
                        .build();
        }
}
