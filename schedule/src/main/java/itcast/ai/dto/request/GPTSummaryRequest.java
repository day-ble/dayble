package itcast.ai.dto.request;

import itcast.ai.Message;

public record GPTSummaryRequest(
        String model,
        Message message,
        float temperature
) {
}
