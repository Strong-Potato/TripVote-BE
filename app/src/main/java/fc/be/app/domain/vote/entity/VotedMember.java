package fc.be.app.domain.vote.entity;

import fc.be.app.domain.member.entity.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
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

    @ManyToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "candidate_id")
    @Comment("투표한 장소")
    private Candidate candidate;

    @ManyToOne
    @JoinColumn(name = "member_id")
    @Comment("투표한 멤버 id")
    private Member member;

    @ManyToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "vote_id")
    @Comment("투표 id")
    private Vote vote;

    @Builder
    private VotedMember(Long id, Candidate candidate, Member member, Vote vote) {
        this.id = id;
        this.candidate = candidate;
        this.member = member;
        this.vote = vote;
    }

    public boolean isMemberVote(Long memberId) {
        return memberId.equals(member.getId());
    }
}
