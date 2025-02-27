package com.dayble.blog.message.controller.dto.request;

public record VerificationRequest(
    String phoneNumber,
    String verificationCode
) {
}
