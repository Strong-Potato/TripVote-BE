package fc.be.openapi.google.dto.review;

import fc.be.openapi.google.dto.review.form.Review;

import java.util.Collections;
import java.util.List;

public record GoogleReviewResponse(
        List<Review> reviews
) {
    public GoogleReviewResponse {
        if (reviews == null) {
            reviews = Collections.emptyList();
        }
    }
}
