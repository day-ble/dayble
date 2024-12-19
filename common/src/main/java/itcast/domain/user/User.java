package itcast.domain.user;

import itcast.domain.BaseEntity;
import itcast.domain.user.enums.ArticleType;
import itcast.domain.user.enums.Interest;
import itcast.domain.user.enums.SendingType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "users")
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String kakaoEmail;

    @Column(unique = true)
    private String email;

    private String nickname;

    @Column(unique = true)
    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    private ArticleType articleType;

    @Enumerated(EnumType.STRING)
    private Interest interest;

    @Enumerated(EnumType.STRING)
    private SendingType sendingType;

    @Builder
    public User(
            Long id,
            String kakaoEmail,
            String email,
            String nickname,
            String phoneNumber,
            ArticleType articleType,
            Interest interest,
            SendingType sendingType
    ) {
        this.id = id;
        this.kakaoEmail = kakaoEmail;
        this.email = email;
        this.nickname = nickname;
        this.phoneNumber = phoneNumber;
        this.articleType = articleType;
        this.interest = interest;
        this.sendingType = sendingType;
    }
}
