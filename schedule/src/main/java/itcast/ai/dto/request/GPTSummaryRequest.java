package itcast.ai.dto.request;

public record GPTSummaryRequest(
        String model,
        Message message,
        float temperature
) {
}
