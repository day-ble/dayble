package com.dayble.blog.mail.controller.response;

public record VerifyMailResponse(
        Boolean isVerify
) {
    public static VerifyMailResponse from(final boolean isVerify) {
        return new VerifyMailResponse(isVerify);
    }
}
