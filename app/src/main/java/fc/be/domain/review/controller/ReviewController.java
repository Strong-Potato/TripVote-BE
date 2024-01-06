package fc.be.domain.review.controller;

import fc.be.domain.review.dto.ReviewCreateRequest;
import fc.be.domain.review.dto.ReviewEditRequest;
import fc.be.domain.review.dto.ReviewResponse;
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

/*        reviewService.createReview(reviewCreateRequest.placeId(), reviewCreateRequest.thumbnail(), reviewCreateRequest.contentTypeId(),
                reviewCreateRequest.title(), reviewCreateRequest.areaCode(), reviewCreateRequest.content(), reviewCreateRequest.rating(),
                reviewCreateRequest.images(), reviewCreateRequest.visitedAt());*/

        return ApiResponse.ok(
                reviewService.createReview(reviewCreateRequest)
        );
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
    public ApiResponse<List<ReviewResponse>> getPlaceReviews(@PathVariable Long placeId) {
        return ApiResponse.ok(reviewService.getPlaceReviews(placeId));
    }

    @GetMapping("/my/{member_Id}")
    public ApiResponse<List<ReviewResponse>> getMemberReviews(@PathVariable Long member_Id) {
        return ApiResponse.ok(reviewService.getMemberReviews(member_Id));
    }

}