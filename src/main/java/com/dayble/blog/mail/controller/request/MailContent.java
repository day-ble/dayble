package com.dayble.blog.mail.controller.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
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
    public static MailContent of(String title, String summary, String originalLink, String thumbnail) {
        return MailContent.builder()
                .title(title)
                .summary(summary)
                .originalLink(originalLink)
                .thumbnail(thumbnail)
                .build();
    }
}
