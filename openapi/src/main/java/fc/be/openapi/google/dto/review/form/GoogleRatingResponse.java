package fc.be.openapi.google.dto.review.form;

public record GoogleRatingResponse(
          Double rating,
          Integer userRatingCount
) {
}
