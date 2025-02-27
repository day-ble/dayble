package com.dayble.blog.message.controller.dto.request;

import java.util.List;

public record SendMessageRequest(
        List<MessageContent> contentList,
        List<RecieverPhoneNumber> phoneNumbers
) {
}
