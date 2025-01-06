package itcast.mail.application;

import static com.slack.api.model.block.Blocks.asBlocks;
import static com.slack.api.model.block.Blocks.divider;
import static com.slack.api.model.block.Blocks.header;
import static com.slack.api.model.block.Blocks.section;
import static com.slack.api.model.block.composition.BlockCompositions.markdownText;
import static com.slack.api.model.block.composition.BlockCompositions.plainText;
import static itcast.exception.ErrorCodes.SLACK_PARSE_ERROR;

import com.slack.api.Slack;
import com.slack.api.methods.MethodsClient;
import com.slack.api.methods.request.chat.ChatPostMessageRequest;
import com.slack.api.methods.response.chat.ChatPostMessageResponse;
import com.slack.api.model.block.composition.TextObject;
import itcast.exception.ItCastApplicationException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SlackService {

    @Value("${slack.token}")
    private String token;
    @Value("${slack.channel.monitor}")
    private String channel;

    public void postInquiry(String receiver) {
        try {
            List<TextObject> textObjects = new ArrayList<>();
            textObjects.add(markdownText("*재 발송 실패 날짜:*\n" + LocalDateTime.now()));
            textObjects.add(markdownText("*재 시도 실패 메일:*\n" + receiver));

            MethodsClient methods = Slack.getInstance().methods(token);
            ChatPostMessageRequest request = ChatPostMessageRequest.builder()
                    .channel(channel)
                    .blocks(asBlocks(
                            header(header ->
                                    header.text(plainText("메일 재 발송에 실패 했습니다!!!"))),
                            divider(),
                            section(section ->
                                    section.fields(textObjects)
                            ))).build();

            ChatPostMessageResponse response = methods.chatPostMessage(request);
            if (!response.isOk()) {
                System.err.println("Slack API Error: " + response.getError());
                throw new ItCastApplicationException(SLACK_PARSE_ERROR);
            }
        } catch (Exception e) {
            System.err.println("Error posting message to Slack: " + e.getMessage());
            throw new ItCastApplicationException(SLACK_PARSE_ERROR);
        }
    }
}
