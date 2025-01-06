package itcast.message.dto.response;

public record VericationResponse(
        Boolean isVerify
) {
    public static VericationResponse from(final boolean isVerify) {
        return new VericationResponse(isVerify);
    }
}
