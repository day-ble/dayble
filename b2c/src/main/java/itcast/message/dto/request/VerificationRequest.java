package itcast.message.dto.request;

public record VerificationRequest (
    String phoneNumber,
    String verificationCode
) {
}