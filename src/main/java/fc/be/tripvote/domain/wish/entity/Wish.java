package fc.be.tripvote.domain.wish.entity;

import fc.be.tripvote.domain.member.entity.Member;
import fc.be.tripvote.domain.place.Place;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;


@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Comment("찜")
public class Wish {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Comment("찜 id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id")
    @Comment("회원 id(FK)")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "place_id")
    @Comment("장소 id(FK)")
    private Place place;
}
