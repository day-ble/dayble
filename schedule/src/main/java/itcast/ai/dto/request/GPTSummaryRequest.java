package itcast.ai.dto.request;

import lombok.Builder;


public record GPTSummaryRequest(
        String model,
        Message message,
        float temperature
) {
    @Builder
    public GPTSummaryRequest(String model, Message message, float temperature) {
        this.model = model;
        this.message = message;
        this.temperature = temperature;
    }
}
