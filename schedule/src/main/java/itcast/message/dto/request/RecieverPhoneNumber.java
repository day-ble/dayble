package itcast.message.dto.request;

import jakarta.validation.constraints.NotBlank;

public record RecieverPhoneNumber(
        @NotBlank
        String phoneNumber
) {
}
