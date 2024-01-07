package fc.be.domain.review.service;

import fc.be.domain.place.Place;
import fc.be.domain.place.entity.*;
import fc.be.domain.review.dto.ReviewCreateRequest;
import fc.be.domain.review.dto.ReviewEditRequest;
import fc.be.domain.review.dto.ReviewResponse;
import fc.be.domain.review.entity.Review;
import fc.be.domain.review.exception.ReviewErrorCode;
import fc.be.domain.review.exception.ReviewException;
import fc.be.domain.review.repository.ReviewRepository;
import fc.be.tourapi.constant.ContentTypeId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReviewService {

    private final ReviewRepository reviewRepository;
    //private final MemberRepository memberRepository;


    @Transactional
    public Review createReview(ReviewCreateRequest reviewCreateRequest) {
        //Member member = memberRepository.findById(memberId); //todo Security 적용
        return reviewRepository.save(
                reviewCreateRequest.toEntity(reviewCreateRequest)
        );
    }

    @Transactional
    public Review editReview(ReviewEditRequest reviewEditRequest) {
        return reviewRepository.save(
                reviewEditRequest.toEntity(reviewEditRequest)
        );
    }

    @Transactional
    public void deleteReview(Long reviewId) {
        reviewRepository.deleteById(reviewId);
    }

    public List<ReviewResponse> getPlaceReviews(Long placeId) {
        return null;
    }

    public ReviewResponse getMemberReviews() {
        Long memberId = 1L; //todo Security 적용
        return ReviewResponse.from(
                reviewRepository.findByMemberId(memberId)
        );
    }
}
