package fc.be.app.domain.review.controller;

import fc.be.app.domain.review.dto.*;
import fc.be.app.domain.review.service.ReviewService;
import fc.be.app.global.config.security.model.user.UserPrincipal;
import fc.be.app.global.http.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping
    public ApiResponse<ReviewCreateResponse> createReview(@Valid @RequestBody ReviewCreateRequest reviewCreateRequest,
                                                          @AuthenticationPrincipal UserPrincipal userPrincipal) {
        return ApiResponse.ok(reviewService.createReview(reviewCreateRequest, userPrincipal));
    }

    @PatchMapping
    public ApiResponse<ReviewEditResponse> editReview(@Valid @RequestBody ReviewEditRequest reviewEditRequest,
                                                      @AuthenticationPrincipal UserPrincipal userPrincipal) {
        return ApiResponse.ok(reviewService.editReview(reviewEditRequest, userPrincipal));
    }

    @DeleteMapping("/{reviewId}")
    public ApiResponse<Integer> deleteReview(@PathVariable Long reviewId,
                                             @AuthenticationPrincipal UserPrincipal userPrincipal) {
        return ApiResponse.ok(reviewService.deleteReview(reviewId, userPrincipal));
    }

    @GetMapping("/rating")
    public ApiResponse<ReviewRatingResponse> getRatingAndReviewCount(
            @Valid @RequestBody ReviewGetRequest reviewGetRequest
    ) {
        return ApiResponse.ok(reviewService.bringReviewRatingAndCount(reviewGetRequest));
    }

    @GetMapping
    public ApiResponse<ReviewGetResponse> getPlaceReviews(
            @Valid @RequestBody ReviewGetRequest reviewGetRequest,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ApiResponse.ok(reviewService.bringReviewInfo(reviewGetRequest,
                PageRequest.of(page, size, Sort.by("visitedAt").descending())));
    }
}
