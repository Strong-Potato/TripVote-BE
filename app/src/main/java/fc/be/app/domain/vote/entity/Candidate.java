package fc.be.app.domain.vote.entity;

import fc.be.app.domain.member.entity.Member;
import fc.be.app.domain.place.Place;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Comment("후보지")
public class Candidate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Comment("후보지 id")
    private Long id;

    @OneToMany(mappedBy = "candidate", cascade = {CascadeType.ALL})
    @Comment("후보에 투표한 회원 id(FK)")
    private List<VotedMember> votedMember;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member owner;

    @ManyToOne
    @JoinColumn(name = "place_id")
    @Comment("후보지의 장소(FK)")
    private Place place;

    @ManyToOne
    @JoinColumn(name = "vote_id")
    @Comment("후보지가 등록된 투표 id(FK)")
    private Vote vote;

    @Comment("후보지에 대한 한줄평")
    private String tagline;

    private int votedCount;

    @Builder
    private Candidate(Long id,
                      List<VotedMember> votedMember,
                      Member owner,
                      Place place,
                      Vote vote,
                      String tagline,
                      int votedCount
    ) {
        this.id = id;
        this.votedMember = votedMember;
        this.owner = owner;
        this.place = place;
        this.vote = vote;
        this.tagline = tagline;
        this.votedCount = votedCount;
    }

    public static Candidate createNewVote(Place place, Member owner, Vote vote, String tagline) {
        return Candidate.builder()
                .place(place)
                .owner(owner)
                .vote(vote)
                .tagline(tagline)
                .votedMember(new ArrayList<>())
                .votedCount(0)
                .build();
    }

    public void vote(Vote vote, Member member) {
        VotedMember voteMember = VotedMember.builder()
                .vote(vote)
                .candidate(this)
                .member(member)
                .build();

        votedMember.add(voteMember);
    }

    public void increaseVoteCount() {
        this.votedCount++;
    }

    public void decreaseVoteCount() {
        this.votedCount--;
    }

    void setVote(Vote vote) {
        this.vote = vote;
    }
}
