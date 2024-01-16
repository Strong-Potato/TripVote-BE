package fc.be.openapi.google.dto.review;

import fc.be.openapi.google.dto.review.form.GoogleRatingResponse;

public record APIRatingResponse(
        Double rating,
        Integer userRatingCount
) {

    public static APIRatingResponse convertToTempRatingResponse(GoogleRatingResponse googleRatingResponse) {
        return new APIRatingResponse(
                googleRatingResponse.rating(),
                googleRatingResponse.userRatingCount()
        );
    }
}
