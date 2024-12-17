package itcast.mail.dto.request;

import jakarta.validation.constraints.NotBlank;

public record MailContent(
        @NotBlank
        String title,
        @NotBlank
        String summary,
        @NotBlank
        String originalLink,
        @NotBlank
        String thumbnail
) {
}
