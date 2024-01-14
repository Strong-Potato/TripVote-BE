package fc.be.app.domain.review.dto.response;

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
}
