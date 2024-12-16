package itcast.user.dto.response;

import itcast.domain.user.User;
import itcast.domain.user.enums.ArticleType;
import itcast.domain.user.enums.Interest;
import itcast.domain.user.enums.SendingType;

public record ProfileUpdateResponse(
        Long id,
        String kakaoEmail,
        String nickname,
        ArticleType articleType,
        Interest interest,
        SendingType sendingType,
        String email
) {
    public static ProfileUpdateResponse fromEntity(User user) {
        return new ProfileUpdateResponse(
                user.getId(),
                user.getKakaoEmail(),
                user.getNickname(),
                user.getArticleType(),
                user.getInterest(),
                user.getSendingType(),
                user.getEmail()
        );
    }
}
