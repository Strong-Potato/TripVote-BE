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
                (googleRatingResponse.rating() == null) ? 0.0 : googleRatingResponse.rating(),
                (googleRatingResponse.reviews() == null)? 0 : googleRatingResponse.reviews().size()
        );
    }
}
