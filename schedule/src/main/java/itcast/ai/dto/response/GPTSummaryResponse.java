package itcast.ai.dto.response;

import java.util.List;

public record GPTSummaryResponse(
        List<Choice> choices
) {
    public record Choice(
            Message message
    ) {
    }

    public record Message(
            String content
    ) {
    }

    public String getCategory() {
        return extractField(1).replace(",", "");
    }

    public String getSummary() {
        return extractField(2).replace(",", "");
    }

    public Integer getRating() {
        return Integer.parseInt(extractField(3));
    }

    private String extractField(final int index) {
        final String content = choices.get(0).message.content();
        return parseContent(content, index);
    }

    private static String parseContent(final String content, final int index) {
        final String[] parts = content.split("\n");
        return parts[index].split(":")[1].trim().replaceAll("\"", "");
    }
}
