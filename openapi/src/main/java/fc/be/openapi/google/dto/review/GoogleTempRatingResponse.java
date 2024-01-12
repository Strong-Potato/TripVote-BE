package fc.be.openapi.google.dto.review;

public record GoogleTempRatingResponse(
        Double rating,
        Integer userRatingCount
) {
}
