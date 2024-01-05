package fc.be.app.domain.space.entity;

import fc.be.app.domain.member.entity.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Comment("여행 스페이스의 초대멤버")
public class JoinedMember {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Comment("초대멤버 id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "space_id")
    @Comment("참여한 여행 스페이스")
    private Space space;

    @ManyToOne
    @JoinColumn(name = "member_id")
    @Comment("여행 스페이스에 참여한 회원")
    private Member member;
}
