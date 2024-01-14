package fc.be.app.domain.review.dto.response;

import fc.be.app.domain.review.entity.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public record ReviewsResponse(
        List<ReviewResponse> reviews,
        Integer pageNumber,
        Integer pageSize,
        Integer totalPages,
        Long totalResult,
        boolean first,
        boolean last
) {
    public static ReviewsResponse from(Page<Review> reviewPage) {
        List<Review> content = reviewPage.getContent();
        Pageable pageableResponse = reviewPage.getPageable();
        int pageNumber = pageableResponse.getPageNumber();
        int pageSize = pageableResponse.getPageSize();
        int totalPages = reviewPage.getTotalPages();
        long totalElements = reviewPage.getTotalElements();
        boolean first = reviewPage.isFirst();
        boolean last = reviewPage.isLast();
        return new ReviewsResponse(content.stream().map(ReviewResponse::of).toList(), pageNumber, pageSize, totalPages, totalElements, first, last);
    }
}
