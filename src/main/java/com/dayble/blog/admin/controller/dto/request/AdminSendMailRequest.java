package com.dayble.blog.admin.controller.dto.request;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AdminSendMailRequest {
    private final String receiver;
    private final List<MailContent> contents;

    @Getter
    @AllArgsConstructor
    public static class MailContent {
        private final Long id;
        private final String title;
        private final String summary;
        private final String originalLink;
        private final String thumbnail;
    }
}
