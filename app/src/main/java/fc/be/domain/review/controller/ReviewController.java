package fc.be.domain.review.controller;

import fc.be.domain.review.dto.ReviewCreateRequest;
import fc.be.domain.review.dto.ReviewEditRequest;
import fc.be.domain.review.dto.ReviewGetResponse;
import fc.be.domain.review.service.ReviewService;
import fc.be.global.http.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping
    public ApiResponse createReview(@Valid @RequestBody ReviewCreateRequest reviewCreateRequest) {
        return ApiResponse.ok(reviewService.createReview(reviewCreateRequest));
    }

    @PatchMapping
    public ApiResponse editReview(@Valid @RequestBody ReviewEditRequest reviewEditRequest) {
        return ApiResponse.ok(reviewService.editReview(reviewEditRequest));
    }

    @DeleteMapping("/{reviewId}")
    public ApiResponse<Void> deleteReview(@PathVariable Long reviewId) {
        reviewService.deleteReview(reviewId);
        return ApiResponse.ok();
    }

    @GetMapping("/{placeId}")
    public ApiResponse<ReviewGetResponse> getPlaceReviews(@PathVariable Long placeId) {
        return ApiResponse.ok(reviewService.getPlaceReviews(placeId));
    }

    @GetMapping("/my")
    public ApiResponse<ReviewGetResponse> getMemberReviews() {
        return ApiResponse.ok(reviewService.getMemberReviews());
    }
}
