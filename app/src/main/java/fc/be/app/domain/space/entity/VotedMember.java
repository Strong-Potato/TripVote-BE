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
@Comment("투표한 인원")
public class VotedMember {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Comment("id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "candidate_id")
    @Comment("투표한 장소")
    private Candidate candidate;

    @ManyToOne
    @JoinColumn(name = "member_id")
    @Comment("투표한 멤버 id")
    private Member member;
}