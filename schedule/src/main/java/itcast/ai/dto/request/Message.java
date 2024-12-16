package itcast.ai.dto.request;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Message {

    private String role;

    private String content;

    public Message(final String role, final String content) {
        this.role = role;
        this.content = content;
    }

    public void addPrompt(final String content) {
        this.content += content;
    }
}
