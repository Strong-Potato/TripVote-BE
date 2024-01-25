package fc.be.app.domain.vote.entity;

import fc.be.app.domain.member.entity.Member;
import fc.be.app.domain.space.entity.Space;
import fc.be.app.domain.space.vo.VoteStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;

import java.util.ArrayList;
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

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
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

    @Builder
    private Vote(Long id, Space space, String title, VoteStatus status, Member owner, List<Candidate> candidates, List<VotedMember> votedMembers) {
        this.id = id;
        this.space = space;
        this.title = title;
        this.status = status;
        this.owner = owner;
        this.candidates = candidates;
        this.votedMembers = votedMembers;
    }

    public void addCandidate(Candidate candidate) {
        candidate.setVote(this);
        this.candidates.add(candidate);
    }

    public boolean isMax(int maxThreshold) {
        return this.candidates.size() == maxThreshold;
    }

    public static Vote of(Space space, String title, Member owner) {
        return Vote.builder()
                .space(space)
                .title(title)
                .owner(owner)
                .candidates(new ArrayList<>())
                .votedMembers(new ArrayList<>())
                .status(VoteStatus.VOTING)
                .build();
    }

    public void changeStatus(VoteStatus voteStatus) {
        this.status = voteStatus;
    }

    public boolean isStillVoting() {
        return this.status == VoteStatus.VOTING;
    }

    public void updateTitle(String title) {
        this.title = title;
    }
}
