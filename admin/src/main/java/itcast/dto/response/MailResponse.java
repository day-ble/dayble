package itcast.dto.response;

import java.time.LocalDate;
import java.util.List;

public record MailResponse(
        Long userId,
        LocalDate createdAt,
        List<MailContent> contents
) {
    public record MailContent(
            Long id,
            String title,
            String summary,
            String originalLink,
            String thumbnail
    ) {}
}
