package fc.be.openapi.google.dto.review;

import fc.be.openapi.google.dto.review.form.Review;

import java.util.ArrayList;
import java.util.List;

public record APIReviewResponse(
        List<Item> reviews
) {
    public static APIReviewResponse convertToAPIReviewResponse(GoogleReviewResponse googleReviewResponse) {
        List<Item> items = new ArrayList<>();

        for (Review review : googleReviewResponse.reviews()) {
            Item item = new Item(
                    review.authorAttribution().displayName(),
                    review.authorAttribution().photoUri(),
                    review.rating(),
                    review.publishTime(),
                    review.originalText().text(),
                    true
            );
            items.add(item);
        }
        return new APIReviewResponse(items);
    }

    public record Item(
            String nickname,
            String profileImage,
            Integer rating,
            String visitedAt,
            String content,
            Boolean isGoogle
    ) {

    }
}
