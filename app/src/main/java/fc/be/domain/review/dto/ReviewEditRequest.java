package fc.be.domain.review.dto;

import fc.be.domain.review.entity.Review;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

import java.time.LocalDate;
import java.util.List;

public record ReviewEditRequest(
        @NotNull Long reviewId,
        @Min(1) Integer rating,
        @NotBlank String content,
        List<String> images,
        @PastOrPresent LocalDate visitedAt
) {

    public Review toEntity(ReviewEditRequest reviewEditRequest) {
        return Review.builder()
                .content(reviewEditRequest.content)
                .images(reviewEditRequest.images)
                .rating(reviewEditRequest.rating)
                .visitedAt(reviewEditRequest.visitedAt)
                .build();
    }
}
