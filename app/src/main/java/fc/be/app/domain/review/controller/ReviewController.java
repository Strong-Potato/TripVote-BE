package fc.be.app.domain.review.controller;

import fc.be.app.domain.review.dto.*;
import fc.be.app.domain.review.service.ReviewService;
import fc.be.app.global.http.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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
    public ApiResponse<Integer> deleteReview(@PathVariable Long reviewId) {
        return ApiResponse.ok(reviewService.deleteReview(reviewId));
    }

    @GetMapping("/rating")
    public ApiResponse<ReviewRatingResponse> getRatingAndReviewCount(
            @Valid @RequestBody ReviewGetRequest reviewGetRequest
    ) {
        return ApiResponse.ok(reviewService.bringJsonReviewRatingAndCount(reviewGetRequest));
    }

    @GetMapping
    public ApiResponse<ReviewGetResponse> getPlaceReviews(
            @Valid @RequestBody ReviewGetRequest reviewGetRequest,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ApiResponse.ok(reviewService.bringJsonReviewInfo(reviewGetRequest,
                PageRequest.of(page, size, Sort.by("visitedAt").descending())));
    }
}
