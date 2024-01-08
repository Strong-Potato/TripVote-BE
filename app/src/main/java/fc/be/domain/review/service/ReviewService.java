package fc.be.domain.review.service;

import fc.be.domain.member.entity.Member;
import fc.be.domain.review.dto.*;
import fc.be.domain.review.entity.Review;
import fc.be.domain.review.exception.ReviewErrorCode;
import fc.be.domain.review.exception.ReviewException;
import fc.be.domain.review.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReviewService {

    private final ReviewRepository reviewRepository;

    @Transactional
    public ReviewCreateResponse createReview(ReviewCreateRequest reviewCreateRequest) {
        //todo [Review] Security 적용 -1
        return ReviewCreateResponse.from(reviewRepository.save(
                reviewCreateRequest.to(reviewCreateRequest, Member.builder().build())));
    }

    @Transactional
    public ReviewEditResponse editReview(ReviewEditRequest reviewEditRequest) {
        Review review = reviewRepository.findById(reviewEditRequest.reviewId())
                .orElseThrow(() -> new ReviewException(ReviewErrorCode.REVIEW_NOT_FOUND));

        review.editReview(reviewEditRequest);
        return ReviewEditResponse.from(reviewRepository.save(review));
    }

    @Transactional
    public void deleteReview(Long reviewId) {

        reviewRepository.deleteById(reviewId);
    }

    public ReviewGetResponse getPlaceReviews(Long placeId) {
        return ReviewGetResponse.from(
                reviewRepository.findByPlaceId(placeId)
        );
    }

    public ReviewGetResponse getMemberReviews() {
        // todo [Review] Security 적용 -2
        return ReviewGetResponse.from(
                reviewRepository.findByMemberId(1L)
        );
    }
}
