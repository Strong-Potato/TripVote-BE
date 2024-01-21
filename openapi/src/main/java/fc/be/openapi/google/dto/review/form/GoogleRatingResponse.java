package fc.be.openapi.google.dto.review.form;

import java.util.List;

public record GoogleRatingResponse(
        List<Review> reviews,
        Double rating,
        Integer userRatingCount
) {
}
