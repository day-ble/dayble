package itcast.message.dto.request;

import jakarta.validation.constraints.NotBlank;

public record MessageContent(
        @NotBlank
        String title,
        @NotBlank
        String content,
        @NotBlank
        String link
){
}
