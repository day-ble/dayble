package com.dayble.blog.message.controller.dto.request;

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
