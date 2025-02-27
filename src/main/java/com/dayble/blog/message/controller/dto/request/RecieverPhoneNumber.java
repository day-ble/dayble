package com.dayble.blog.message.controller.dto.request;

import jakarta.validation.constraints.NotBlank;

public record RecieverPhoneNumber(
        @NotBlank
        String phoneNumber
) {
}
