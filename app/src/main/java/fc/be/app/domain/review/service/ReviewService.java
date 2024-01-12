package fc.be.app.domain.review.service;

import fc.be.app.domain.member.entity.Member;
import fc.be.app.domain.member.repository.MemberRepository;
import fc.be.app.domain.place.service.PlaceService;
import fc.be.app.domain.review.dto.*;
import fc.be.app.domain.review.entity.Review;
import fc.be.app.domain.review.exception.ReviewErrorCode;
import fc.be.app.domain.review.exception.ReviewException;
import fc.be.app.domain.review.repository.ReviewRepository;
import fc.be.app.global.http.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final PlaceService placeService;
    private final MemberRepository memberRepository;

    @Transactional
    public ReviewCreateResponse createReview(ReviewCreateRequest reviewCreateRequest) {
        placeService.saveOrUpdatePlace(reviewCreateRequest.placeId(),
                reviewCreateRequest.contentTypeId());

        //todo [Review] Security 적용 -1
        Member member = memberRepository.save(Member.builder().build());
        return ReviewCreateResponse.from(
                reviewRepository.save(reviewCreateRequest.to(member)));
    }

    @Transactional
    public ApiResponse<ReviewEditResponse> editReview(ReviewEditRequest reviewEditRequest) {
        Review review = reviewRepository.findById(reviewEditRequest.reviewId())
                .orElseThrow(() -> new ReviewException(ReviewErrorCode.REVIEW_NOT_FOUND));

        review.editReview(reviewEditRequest);
        return ApiResponse.ok(ReviewEditResponse.from(reviewRepository.save(review)));
    }

    @Transactional
    public void deleteReview(Long reviewId) {

        reviewRepository.deleteById(reviewId);
    }

    public ApiResponse<ReviewGetResponse> getPlaceReviews(Integer placeId) {
        return ApiResponse.ok(ReviewGetResponse.from(
                reviewRepository.findByPlaceId(placeId)
        ));
    }

    public ApiResponse<ReviewGetResponse> getMemberReviews() {
        // todo [Review] Security 적용 -2
        return ApiResponse.ok(ReviewGetResponse.from(
                reviewRepository.findByMemberId(1L)
        ));
    }
}
