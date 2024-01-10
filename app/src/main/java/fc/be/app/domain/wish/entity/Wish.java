package fc.be.app.domain.wish.entity;

import fc.be.app.domain.place.Place;
import fc.be.app.global.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Comment;

@IdClass(WishId.class)
@SuperBuilder
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Comment("찜")
public class Wish extends BaseTimeEntity {
    @Id
    @Comment("회원 id(FK)")
    private Long memberId;

    @Id
    @ManyToOne
    @JoinColumn(name = "place_id")
    @Comment("장소 id(FK)")
    private Place place;
}