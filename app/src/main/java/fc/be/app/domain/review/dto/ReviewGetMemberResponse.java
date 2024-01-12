package fc.be.app.domain.review.dto;

import fc.be.app.domain.review.entity.Review;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public record ReviewGetMemberResponse(List<Item> reviews) {

    public static ReviewGetMemberResponse from(
            List<Review> reviews
    ) {
        List<Item> items = new ArrayList<>();
        for (var review : reviews) {
            items.add(
                    new Item(
                            review.getId(),
                            review.getPlace().getThumbnail(),
                            review.getPlace().getTitle(),
                            review.getPlace().getContentTypeId().getId(),
                            review.getPlace().getLocation().getAreaCode(),
                            review.getRating(),
                            review.getVisitedAt(),
                            review.getContent()
                    )
            );
        }
        return new ReviewGetMemberResponse(
                items
        );
    }

    private record Item(
            Long reviewId,
            String thumbnail,
            String placeTitle,
            Integer contentTypeId,
            Integer areaCode,
            Integer rating,
            LocalDate visitedAt,
            String content
    ) {
    }
}
