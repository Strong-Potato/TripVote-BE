package fc.be.app.domain.review.dto;


import fc.be.app.domain.member.entity.Member;
import fc.be.app.domain.place.Place;
import fc.be.app.domain.review.entity.Review;
import jakarta.validation.constraints.*;

import java.time.LocalDate;
import java.util.List;

import static java.lang.Boolean.FALSE;

public record ReviewCreateRequest(
        @Positive Integer placeId,
        @NotNull Integer contentTypeId,
        @NotNull String title,
        @Min(1) @Max(5) Integer rating,
        @NotBlank String content,
        List<String> images,
        @PastOrPresent LocalDate visitedAt
) {

    public Review to(Member member, Place place) {
        return Review.builder()
                .member(member)
                .place(place)
                .visitedAt(visitedAt)
                .rating(rating)
                .content(content)
                .images(images)
                .isGoogle(FALSE) //앱 내 리뷰이므로 기본 값 FALSE
                .build();
    }
}
