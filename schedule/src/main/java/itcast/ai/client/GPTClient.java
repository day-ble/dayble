package itcast.ai.client;

import itcast.ai.Message;
import itcast.ai.dto.request.GPTSummaryRequest;
import itcast.ai.dto.response.GPTSummaryResponse;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class GPTClient {

    private final WebClient webClient;

    @Value("${openai.secret-key}")
    private String secretKey;

    @Value("${openai.prompt}")
    private String prompt;

    public GPTClient() {
        this.webClient = WebClient.builder()
                .baseUrl("https://api.openai.com")
                .build();
    }

    public GPTSummaryResponse sendRequest(final GPTSummaryRequest gptSummaryRequest) {
        return webClient.post()
                .uri("/v1/chat/completions")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + secretKey)
                .header(HttpHeaders.CONTENT_TYPE, "application/json")
                .bodyValue(toRequestBody(gptSummaryRequest))
                .retrieve()
                .bodyToMono(GPTSummaryResponse.class)
                .block();
    }

    private Map<String, Object> toRequestBody(final GPTSummaryRequest gptSummaryRequest) {
        return Map.of(
                "model", gptSummaryRequest.model(),
                "messages", toMessages(gptSummaryRequest.message()),
                "temperature", gptSummaryRequest.temperature()
        );
    }

    private List<Message> toMessages(final Message message) {
        message.addPrompt(prompt);
        return List.of(message);
    }
}
