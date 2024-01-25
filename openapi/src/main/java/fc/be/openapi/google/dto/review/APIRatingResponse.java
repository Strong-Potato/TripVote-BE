package fc.be.openapi.google.dto.review;

import fc.be.openapi.google.dto.review.form.GoogleRatingResponse;

public record APIRatingResponse(
        Double rating,
        Integer userRatingCount
) {

    public static APIRatingResponse convertToRatingResponse(GoogleRatingResponse googleRatingResponse) {
        if(googleRatingResponse == null){
            return new APIRatingResponse(0.0, 0);
        }

        return new APIRatingResponse(
                googleRatingResponse.rating(),
                googleRatingResponse.reviews().size()
        );
    }
}
