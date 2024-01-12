package fc.be.app.domain.review.dto;

import fc.be.app.domain.member.entity.Member;
import fc.be.app.domain.place.Place;

import fc.be.app.domain.review.entity.Review;

import fc.be.openapi.tourapi.constant.ContentTypeId;
import jakarta.validation.constraints.*;
import java.time.LocalDate;
import java.util.List;

public record ReviewCreateRequest(
        @NotNull Integer placeId,
        @NotNull String thumbnail,
        @NotNull Integer contentTypeId,
        @NotNull String title,
        @NotNull Integer areaCode,
        @Min(1) Integer rating,
        @NotBlank String content,
        List<String> images,
        @PastOrPresent LocalDate visitedAt
) {

    // todo [Review] Member 관련 Security 확인 되는 대로 dto 수정 하겠음 - 민균
    public Review to(Member member) {
        return Review.builder()
                .member(member)
                .place(Place.builder().id(placeId).build())
                .visitedAt(visitedAt)
                .rating(rating)
                .content(content)
                .images(images)
                .build();
    }
}
