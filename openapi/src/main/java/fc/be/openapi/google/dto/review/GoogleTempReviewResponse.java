package fc.be.openapi.google.dto.review;

import java.util.List;

public record GoogleTempReviewResponse(
        List<Item> reviews
) {
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
