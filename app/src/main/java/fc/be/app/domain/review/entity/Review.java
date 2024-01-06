package fc.be.app.domain.review.entity;

import fc.be.app.domain.member.entity.Member;
import fc.be.app.domain.place.Place;
import fc.be.app.global.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Comment;

import java.time.LocalDateTime;

@Entity
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Comment("장소 후기(from Google 리뷰)")
public class Review extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Comment("리뷰 id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id")
    @Comment("작성자")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "place_id")
    @Comment("장소")
    private Place place;

    @Comment("리뷰 별점")
    private Integer rating;

    @Comment("리뷰 내용")
    private String content;

    @Column(length = Short.MAX_VALUE)
    @Comment("리뷰 이미지")
    private String images;

    @Comment("리뷰 작성 시각")
    private LocalDateTime publishTime;
}
