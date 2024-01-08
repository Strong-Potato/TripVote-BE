package fc.be.domain.review.dto;

import fc.be.app.domain.member.entity.Member;
import fc.be.app.domain.place.Place;
import fc.be.domain.review.entity.Review;

import fc.be.openapi.tourapi.constant.ContentTypeId;
import jakarta.validation.constraints.*;
import java.time.LocalDate;
import java.util.List;

public record ReviewCreateRequest(
        @NotNull Integer placeId,
        @NotNull String thumbnail,
        @NotNull ContentTypeId contentTypeId,
        @NotNull String title,
        @NotNull Integer areaCode,
        @Min(1) Integer rating,
        @NotBlank String content,
        List<String> images,
        @PastOrPresent LocalDate visitedAt
) {

    // todo [Review] Member 관련 Security 확인 되는 대로 dto 수정 하겠음 - 민균
    public Review to(ReviewCreateRequest reviewCreateRequest, Member member) {
        return Review.builder()
                .member(member)
                .place(convertPlace(reviewCreateRequest))
                .visitedAt(reviewCreateRequest.visitedAt)
                .rating(reviewCreateRequest.rating)
                .content(reviewCreateRequest.content)
                .images(images)
                .build();
    }

    private Place convertPlace(ReviewCreateRequest reviewCreateRequest){
        return Place.builder()
                .id(reviewCreateRequest.placeId)
                .contentTypeId(reviewCreateRequest.contentTypeId)
                .title(reviewCreateRequest.title)
                .thumbnail(reviewCreateRequest.thumbnail)
                .build();
    }
}
