package fc.be.app.domain.member.dto.response;

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
            PlaceResponse place,
            LocalDate visitedAt,
            Integer rating,
            String content
    ) {
        public record PlaceResponse(
                Long id,
                String area,
                String category,
                String title,
                String thumbnail
        ) {
            public static PlaceResponse from(ReviewResponse.PlaceInfo placeInfo) {
                return new PlaceResponse(
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
                    PlaceResponse.from(reviewResponse.place()),
                    reviewResponse.visitedAt(),
                    reviewResponse.rating(),
                    reviewResponse.content()
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
