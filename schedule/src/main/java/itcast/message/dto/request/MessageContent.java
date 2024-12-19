package itcast.message.dto.request;

import jakarta.validation.constraints.NotBlank;

public record MessageContent(
        @NotBlank
        String title,
        @NotBlank
        String summary,
        @NotBlank
        String originalLink
){
}
