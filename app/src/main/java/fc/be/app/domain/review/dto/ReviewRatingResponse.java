package fc.be.app.domain.review.dto;

public record ReviewRatingResponse(
        Double rating,
        Long userRatingCount
) {
}
