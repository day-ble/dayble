package itcast.user.dto.request;

import itcast.domain.user.User;
import itcast.domain.user.enums.ArticleType;
import itcast.domain.user.enums.Interest;
import itcast.domain.user.enums.SendingType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record ProfileCreateRequest(
        @NotBlank(message = "닉네임을 입력해주세요.")
        String nickname,
        @NotNull(message = "기사 타입을 선택해주세요.")
        ArticleType articleType,
        @NotNull(message = "관심분야를 선택해주세요.")
        Interest interest,
        @NotNull(message = "발송 타입을 선택해주세요.")
        SendingType sendingType,
        @Email
        String email,
        @Pattern(regexp = "^010-[0-9]{4}-[0-9]{4}$", message = "올바른 휴대폰 번호 형식이 아닙니다. 예: 010-1234-5678")
        String phoneNumber
) {
    public User toEntity(User existingUser) {
        return User.builder()
                .id(existingUser.getId())
                .kakaoEmail(existingUser.getKakaoEmail())
                .nickname(nickname)
                .articleType(articleType)
                .interest(interest)
                .sendingType(sendingType)
                .email(email)
                .phoneNumber(phoneNumber)
                .build();
    }
}
