package fc.be.app.domain.member.entity;

import fc.be.app.domain.member.vo.AuthProvider;
import fc.be.app.domain.member.vo.MemberStatus;
import fc.be.app.global.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Comment;

@Entity
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@EqualsAndHashCode(callSuper = false, of = {"id"})
@Comment("회원")
public class Member extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Comment("회원 아이디")
    private Long id;

    @Comment("회원 이메일")
    private String email;

    @Comment("회원 비밀번호")
    private String password;

    @Comment("회원 닉네임")
    private String nickname;

    @Comment("프로필 이미지")
    private String profile;

    @Comment("회원가입 경로")
    @Enumerated(EnumType.STRING)
    private AuthProvider provider;

    @Comment("회원정보 제공자가 제공하는 id")
    private String providedId;

    @Comment("활성화 상태")
    @Enumerated(EnumType.STRING)
    private MemberStatus status = MemberStatus.ACTIVATED;

    @Builder
    private Member(String email, String password, String nickname, String profile, AuthProvider provider, String providedId) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.provider = provider;
        this.providedId = providedId;
        this.profile = profile;
    }

    public void changePassword(String password) {
        this.password = password;
    }

    public void changeInfo(String newNickname, String newProfile) {
        this.nickname = newNickname;
        this.profile = newProfile;
    }

    public void deactivate() {
        this.status = MemberStatus.DEACTIVATED;
    }
}
