package fc.be.app.domain.review.controller;

import fc.be.app.domain.review.dto.ReviewCreateRequest;
import fc.be.app.domain.review.dto.ReviewCreateResponse;
import fc.be.app.domain.review.dto.ReviewEditRequest;
import fc.be.app.domain.review.dto.ReviewGetResponse;
import fc.be.app.domain.review.service.ReviewService;
import fc.be.app.global.http.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping
    public ApiResponse<ReviewCreateResponse> createReview(@Valid @RequestBody ReviewCreateRequest reviewCreateRequest) {
        return ApiResponse.ok(reviewService.createReview(reviewCreateRequest));
    }

    @PatchMapping
    public ApiResponse<ReviewCreateResponse> editReview(@Valid @RequestBody ReviewEditRequest reviewEditRequest) {
        return ApiResponse.ok(reviewService.editReview(reviewEditRequest));
    }

    @DeleteMapping("/{reviewId}")
    public ApiResponse<Void> deleteReview(@PathVariable Long reviewId) {
        reviewService.deleteReview(reviewId);
        return ApiResponse.ok();
    }

    @GetMapping("/{placeId}")
    public ApiResponse<ReviewGetResponse> getPlaceReviews(@PathVariable Integer placeId) {
        return ApiResponse.ok(reviewService.getPlaceReviews(placeId));
    }

    @GetMapping("/my")
    public ApiResponse<ReviewGetResponse> getMemberReviews() {
        return ApiResponse.ok(reviewService.getMemberReviews());
    }
}
