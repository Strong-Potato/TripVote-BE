package fc.be.app.domain.vote.entity;

import fc.be.app.domain.member.entity.Member;
import fc.be.app.domain.space.entity.Space;
import fc.be.app.domain.space.vo.VoteStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@EqualsAndHashCode(callSuper = false, of = {"id"})
@Comment("투표")
public class Vote {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Comment("투표 id")
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "space_id")
    @Comment("여행스페이스 식별자")
    private Space space;

    @Comment("투표명")
    private String title;

    @Enumerated(EnumType.STRING)
    @Comment("투표상태")
    private VoteStatus status;

    @ManyToOne
    @JoinColumn(name = "member_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    @Comment("투표를 만든 사람")
    private Member owner;

    @OneToMany(mappedBy = "vote")
    @Comment("후보지")
    private List<Candidate> candidates;

    @OneToMany(mappedBy = "vote")
    private List<VotedMember> votedMembers;

    public void addCandidate(Candidate candidate) {
        candidate.setVote(this);
        this.candidates.add(candidate);
    }
}
