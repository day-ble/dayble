package com.dayble.blog.mail.controller.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import java.util.List;
import lombok.Builder;

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
