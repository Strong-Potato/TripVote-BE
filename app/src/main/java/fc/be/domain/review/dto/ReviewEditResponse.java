package fc.be.domain.review.dto;

import fc.be.domain.review.entity.Review;

import java.time.LocalDate;
import java.util.List;

public record ReviewEditResponse(
        Long reviewId,
        Integer rating,
        String content,
        List<String> images,
        LocalDate visitedAt
) {
    public static ReviewEditResponse from(Review review) {
        return new ReviewEditResponse(
                review.getId(),
                review.getRating(),
                review.getContent(),
                review.getImages(),
                review.getVisitedAt()
        );
    }
}
