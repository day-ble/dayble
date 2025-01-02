package itcast.dto.response;

import itcast.domain.newsHistory.NewsHistory;
import java.time.LocalDateTime;

public record AdminNewsHistoryResponse (
        Long id,
        Long userId,
        Long newsId,
        LocalDateTime createdAt,
        LocalDateTime modifiedAt
) {
    public AdminNewsHistoryResponse(NewsHistory newsHistory) {
        this(
                newsHistory.getId(),
                newsHistory.getUser().getId(),
                newsHistory.getNews().getId(),
                newsHistory.getCreatedAt(),
                newsHistory.getModifiedAt()
        );
    }
}
