package fc.be.tripvote.domain.member.entity;

import fc.be.tripvote.global.entity.BaseTimeEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Comment;

@SuperBuilder
@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = {"id"})
@Comment("회원")
public class Member extends BaseTimeEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Comment("회원 아이디")
    private Long id;

    @Comment("회원 이메일")
    private String email;

    @Comment("회원 비밀번호")
    private String password;

    @Comment("회원 실명")
    private String name;

    @Comment("회원 닉네임")
    private String nickname;
}
