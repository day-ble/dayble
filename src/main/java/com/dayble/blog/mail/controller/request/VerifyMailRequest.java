package com.dayble.blog.mail.controller.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record VerifyMailRequest(
        @Email
        String email,
        @NotBlank
        String authenticationCode
) {
}
