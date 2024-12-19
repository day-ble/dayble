package itcast.message.dto.request;

import java.util.List;

public record SendMessageRequest(
        List<MessageContent> contentList,
        List<RecieverPhoneNumber> phoneNumbers
) {
}
