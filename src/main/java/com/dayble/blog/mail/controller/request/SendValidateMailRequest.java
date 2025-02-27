package com.dayble.blog.mail.controller.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record SendValidateMailRequest(
        @Email
        String receiver,
        @NotBlank
        String authenticationCode
) {
    public static SendValidateMailRequest of(final String email, final String authenticationCode) {
        return new SendValidateMailRequest(email, authenticationCode);
    }
}
