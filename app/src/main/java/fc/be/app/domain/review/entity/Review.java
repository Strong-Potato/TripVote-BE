package fc.be.app.domain.review.entity;


import fc.be.app.domain.member.entity.Member;
import fc.be.app.domain.place.Place;
import fc.be.app.domain.review.dto.ReviewEditRequest;
import fc.be.app.global.entity.BaseTimeEntity;
import fc.be.app.global.util.ListImagesConverter;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Comment;

import java.time.LocalDate;
import java.util.List;

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

    @Convert(converter = ListImagesConverter.class)
    @Column(length = Short.MAX_VALUE)
    @Comment("리뷰 이미지")
    private List<String> images;

    @Comment("방문 날짜")
    private LocalDate visitedAt;

    @Comment("구글 리뷰 여부")
    private Boolean isGoogle;

    @Builder
    public Review(Member member, Place place, Integer rating, String content, List<String> images, LocalDate visitedAt, Boolean isGoogle) {
        this.member = member;
        this.place = place;
        this.rating = rating;
        this.content = content;
        this.images = images;
        this.visitedAt = visitedAt;
        this.isGoogle = isGoogle;
    }

    public void editReview(ReviewEditRequest reviewEditRequest) {
        this.rating = reviewEditRequest.rating();
        this.content = reviewEditRequest.content();
        this.images = reviewEditRequest.images();
        this.visitedAt = reviewEditRequest.visitedAt();
    }
}
