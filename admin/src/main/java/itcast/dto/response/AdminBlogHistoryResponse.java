package itcast.dto.response;

import itcast.domain.blogHistory.BlogHistory;
import java.time.LocalDateTime;

public record AdminBlogHistoryResponse (
        Long id,
        Long userId,
        Long blogId,
        LocalDateTime createdAt,
        LocalDateTime modifiedAt
) {
    public AdminBlogHistoryResponse(BlogHistory blogHistory) {
        this(
                blogHistory.getId(),
                blogHistory.getUser().getId(),
                blogHistory.getBlog().getId(),
                blogHistory.getCreatedAt(),
                blogHistory.getModifiedAt()
        );
    }
}
