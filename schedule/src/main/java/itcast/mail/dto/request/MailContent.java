package itcast.mail.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

public record MailContent(
        @NotBlank
        String title,
        @NotBlank
        String summary,
        @NotBlank
        String originalLink,
        @NotBlank
        String thumbnail
) {
        @Builder
        public MailContent(
                String title,
                String summary,
                String originalLink,
                String thumbnail) {
                this.title = title;
                this.summary = summary;
                this.originalLink = originalLink;
                this.thumbnail = thumbnail;
        }
}
