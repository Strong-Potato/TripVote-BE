package fc.be.app.domain.review.dto;

import fc.be.app.domain.review.entity.Review;

import java.time.LocalDate;
import java.util.List;

public record ReviewCreateResponse(
        Long reviewId,
        Integer placeId,
        Integer rating,
        String content,
        List<String> images,
        LocalDate visitedAt
) {
    public static ReviewCreateResponse from(Review review) {
        return new ReviewCreateResponse(
                review.getId(),
                review.getPlace().getId(),
                review.getRating(),
                review.getContent(),
                review.getImages(),
                review.getVisitedAt()
        );
    }
}
