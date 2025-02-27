package com.dayble.blog.user.controller.dto.response;

import com.dayble.blog.user.domain.User;
import com.dayble.blog.user.domain.enums.ArticleType;
import com.dayble.blog.user.domain.enums.Interest;
import com.dayble.blog.user.domain.enums.SendingType;

public record ProfileUpdateResponse(
        Long id,
        String kakaoEmail,
        String nickname,
        ArticleType articleType,
        Interest interest,
        SendingType sendingType,
        String email,
        String phoneNumber
) {
    public static ProfileUpdateResponse fromEntity(User user) {
        return new ProfileUpdateResponse(
                user.getId(),
                user.getKakaoEmail(),
                user.getNickname(),
                user.getArticleType(),
                user.getInterest(),
                user.getSendingType(),
                user.getEmail(),
                user.getPhoneNumber()
        );
    }
}
