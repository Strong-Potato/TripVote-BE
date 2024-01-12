package fc.be.app.domain.vote.entity;

import fc.be.app.domain.member.entity.Member;
import fc.be.app.domain.place.Place;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

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

    @ManyToOne
    @JoinColumn(name = "member_id")
    @Comment("작성자(FK)")
    private Member member;

    @OneToMany(mappedBy = "candidate")
    @JoinColumn(name = "voted_id")
    @Comment("후보에 투표한 회원 id(FK)")
    private List<VotedMember> votedMember;

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

    @Builder
    private Candidate(Long id, Member member, List<VotedMember> votedMember, Place place, Vote vote, String tagline) {
        this.id = id;
        this.member = member;
        this.votedMember = votedMember;
        this.place = place;
        this.vote = vote;
        this.tagline = tagline;
    }

    public void vote(Vote vote, Member member) {
        VotedMember voteMember = VotedMember.builder()
                .vote(vote)
                .candidate(this)
                .member(member)
                .build();

        votedMember.add(voteMember);
    }

    void setVote(Vote vote) {
        this.vote = vote;
    }
}
