package itcast.blog.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Configuration
public class VelogHttpClient {

    private static final String BASE_URL = "https://v3.velog.io/graphql";
    private final WebClient webClient;
    private final ObjectMapper objectMapper;

    public VelogHttpClient() {
        this.webClient = WebClient.builder()
                .baseUrl(BASE_URL)
                .defaultHeader("accept", "*/*")
                .defaultHeader("user-agent", "Mozilla/5.0 (Linux; Android 6.0; Nexus 5 Build/MRA58N) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/131.0.0.0 Mobile Safari/537.36")
                .defaultHeader("content-type", "application/json")
                .build();
        this.objectMapper = new ObjectMapper();
    }

    public String fetchTrendingPostsOfJson(final String query, final String variables) {
        try {
            final Map<String, Object> payload = new HashMap<>();
            payload.put("query", query);
            payload.put("variables", objectMapper.readValue(variables, Map.class));

            final String body = objectMapper.writeValueAsString(payload);

            return webClient.post()
                    .bodyValue(body)
                    .retrieve()
                    .onStatus(HttpStatusCode::isError, clientResponse -> {
                        log.error("Error Status: {}", clientResponse.statusCode());
                        return clientResponse.createException().flatMap(Mono::error);
                    })
                    .bodyToMono(String.class)
                    .block();
        } catch (WebClientResponseException e) {
            log.error("Error response: {} - {}", e.getRawStatusCode(), e.getResponseBodyAsString());
            throw new RuntimeException("Failed to fetch posts", e);
        } catch (Exception e) {
            log.error("Unexpected error", e);
            throw new RuntimeException("Unexpected error", e);
        }
    }
}

