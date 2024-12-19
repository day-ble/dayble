package itcast.user.dto.request;

import itcast.domain.user.User;
import itcast.domain.user.enums.ArticleType;
import itcast.domain.user.enums.Interest;
import itcast.domain.user.enums.SendingType;

public record ProfileUpdateRequest(
        String nickname,
        ArticleType articleType,
        Interest interest,
        SendingType sendingType,
        String email,
        String phoneNumber
) {
    public User toEntity(User existingUser) {
        return User.builder()
                .id(existingUser.getId()) // 기존 ID 유지
                .kakaoEmail(existingUser.getKakaoEmail()) // 기존 카카오 이메일 유지
                .nickname(isValid(nickname) ? nickname : existingUser.getNickname())
                .articleType(articleType != null ? articleType : existingUser.getArticleType())
                .interest(interest != null ? interest : existingUser.getInterest())
                .sendingType(sendingType != null ? sendingType : existingUser.getSendingType())
                .email(isValid(email) ? email : existingUser.getEmail())
                .phoneNumber(isValid(phoneNumber) ? phoneNumber : existingUser.getPhoneNumber())
                .build();
    }

    private boolean isValid(String value) {
        return value != null && !value.trim().isEmpty();
    }
}