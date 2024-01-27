package fc.be.openapi.google.dto.review;

import fc.be.openapi.google.dto.review.form.Review;

import java.util.ArrayList;
import java.util.List;

public record APIReviewResponse(
        List<APIReviewItem> reviews
) {
    public static APIReviewResponse convertToAPIReviewResponse(GoogleReviewResponse googleReviewResponse) {
        List<APIReviewItem> APIReviewItems = new ArrayList<>();

        for (Review review : googleReviewResponse.reviews()) {
            APIReviewItem APIReviewItem = new APIReviewItem(
                    review.authorAttribution().displayName(),
                    review.authorAttribution().photoUri(),
                    review.rating(),
                    review.publishTime(),
                    (review.originalText() == null) ? "" : review.originalText().text(),
                    true,
                    List.of()
            );

            APIReviewItems.add(APIReviewItem);
        }
        return new APIReviewResponse(APIReviewItems);
    }

    public record APIReviewItem(
            String nickname,
            String profileImage,
            Integer rating,
            String visitedAt,
            String content,
            Boolean isGoogle,
            List<String> images
    ) {
    }
}
