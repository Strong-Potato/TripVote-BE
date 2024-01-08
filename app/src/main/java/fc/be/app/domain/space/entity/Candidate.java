package fc.be.app.domain.space.entity;

import fc.be.app.domain.member.entity.Member;
import fc.be.domain.place.Place;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

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

    @ManyToOne
    @JoinColumn(name = "voted_id")
    @Comment("후보에 투표한 회원 id(FK)")
    private VotedMember votedMember;

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
}
