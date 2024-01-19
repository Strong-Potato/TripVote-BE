package fc.be.app.domain.space.entity;

import fc.be.app.domain.member.entity.Member;
import fc.be.app.global.entity.BaseTimeEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Comment("여행 스페이스의 초대멤버")
public class JoinedMember extends BaseTimeEntity {

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

    @Comment("스페이스 나가기 여부")
    private boolean leftSpace;

    @Builder
    private JoinedMember(Space space, Member member, boolean leftSpace) {
        this.space = space;
        this.member = member;
        this.leftSpace = leftSpace;
    }

    public static JoinedMember create(Space space, Member member) {
        return JoinedMember.builder()
                .space(space)
                .member(member)
                .leftSpace(false)
                .build();
    }

    public void updateLeftSpace(Boolean leftSpace) {
        this.leftSpace = leftSpace;
    }
}
