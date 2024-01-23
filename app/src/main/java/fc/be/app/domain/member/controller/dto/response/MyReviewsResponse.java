package fc.be.app.domain.member.controller.dto.response;

import fc.be.app.domain.review.dto.response.ReviewResponse;
import fc.be.app.domain.review.dto.response.ReviewsResponse;

import java.time.LocalDate;
import java.util.List;

public record MyReviewsResponse(
        List<MyReview> reviews,
        Integer pageNumber,
        Integer pageSize,
        Integer totalPages,
        Long totalResult,
        boolean first,
        boolean last
) {
    public record MyReview(
            Long id,
            ReviewedPlace place,
            LocalDate visitedAt,
            Integer rating,
            String content,
            List<String> images
    ) {
        public record ReviewedPlace(
                Long id,
                String area,
                String category,
                String title,
                String thumbnail
        ) {
            public static ReviewedPlace from(ReviewResponse.PlaceInfo placeInfo) {
                return new ReviewedPlace(
                        placeInfo.id().longValue(),
                        placeInfo.area(),
                        placeInfo.category(),
                        placeInfo.title(),
                        placeInfo.thumbnail()
                );
            }
        }

        public static MyReview from(ReviewResponse reviewResponse) {
            return new MyReview(
                    reviewResponse.id(),
                    ReviewedPlace.from(reviewResponse.place()),
                    reviewResponse.visitedAt(),
                    reviewResponse.rating(),
                    reviewResponse.content(),
                    reviewResponse.images()
            );
        }
    }

    public static MyReviewsResponse from(ReviewsResponse reviewsResponse) {
        return new MyReviewsResponse(
                reviewsResponse.reviews().stream().map(MyReview::from).toList(),
                reviewsResponse.pageNumber(),
                reviewsResponse.pageSize(),
                reviewsResponse.totalPages(),
                reviewsResponse.totalResult(),
                reviewsResponse.first(),
                reviewsResponse.last()
        );
    }
}
