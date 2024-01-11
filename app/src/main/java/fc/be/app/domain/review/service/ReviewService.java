package fc.be.app.domain.review.service;

import fc.be.app.domain.member.entity.Member;
import fc.be.app.domain.member.repository.MemberRepository;
import fc.be.app.domain.place.service.PlaceService;
import fc.be.app.domain.review.dto.*;
import fc.be.app.domain.review.entity.Review;
import fc.be.app.domain.review.exception.ReviewErrorCode;
import fc.be.app.domain.review.exception.ReviewException;
import fc.be.app.domain.review.repository.ReviewRepository;
import fc.be.openapi.google.GooglePlacesService;
import fc.be.openapi.google.ReviewJsonReader;
import fc.be.openapi.google.dto.review.GoogleReviewResponse;
import fc.be.openapi.google.dto.review.form.GoogleRatingResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final MemberRepository memberRepository;
    private final PlaceService placeService;
    private final GooglePlacesService googlePlacesService;

    @Transactional
    public ReviewCreateResponse createReview(ReviewCreateRequest reviewCreateRequest) {
        //todo [Review] Security 적용 -1
        var member = memberRepository.save(memberRepository.save(Member.builder().build()));
        Integer placeId = reviewCreateRequest.placeId();
        Integer contentTypeId = reviewCreateRequest.contentTypeId();

        placeService.saveOrUpdatePlace(placeId, contentTypeId);

        return ReviewCreateResponse.from(reviewRepository.save(
                reviewCreateRequest.to(member)));
    }

    @Transactional
    public ReviewEditResponse editReview(ReviewEditRequest reviewEditRequest) {
        Review review = reviewRepository.findById(reviewEditRequest.reviewId())
                .orElseThrow(() -> new ReviewException(ReviewErrorCode.REVIEW_NOT_FOUND));

        review.editReview(reviewEditRequest);
        return ReviewEditResponse.from(reviewRepository.save(review));
    }

    public ReviewGetMemberResponse getMemberReviews(Pageable pageable) {
        Long memberId = 1L;
        // todo [Review] Security 적용 -2
        return ReviewGetMemberResponse.from(
                reviewRepository.findByMemberId(memberId, pageable).toList());
    }

    @Transactional
    public Integer deleteReview(Long reviewId) {
        Long memberId = 1L;
        return reviewRepository.deleteByIdAndMemberId(reviewId, memberId);
    }

    // 구글 API를 호출하는 실제 메서드
    public ReviewGetResponse bringReviewInfo(ReviewGetRequest reviewGetRequest, Pageable pageable) {
        Integer placeId = reviewGetRequest.placeId();

        if (pageable.getPageNumber() == 0) {
            GoogleReviewResponse response = googlePlacesService.bringGoogleReview(reviewGetRequest.placeTitle());
            List<Review> reviews = new ArrayList<>(ReviewGetResponse.convertToReviews(response));

            reviews.addAll(reviewRepository.findByPlaceId(placeId, pageable).toList());
            return ReviewGetResponse.from(reviews);
        }
        return ReviewGetResponse.from(reviewRepository.findByPlaceId(placeId, pageable).toList());
    }

    // 구글 API를 호출하는 실제 메서드
    public ReviewRatingResponse bringReviewRatingAndCount(ReviewGetRequest reviewGetRequest) {
        GoogleRatingResponse googleReviewResponse = googlePlacesService.bringGoogleRatingCount(reviewGetRequest.placeTitle());
        double googleRating = googleReviewResponse.rating();
        long googleCount = googleReviewResponse.userRatingCount();

        List<Number> tripVoteResponse = reviewRepository.countAndAverageRatingByPlaceId(reviewGetRequest.placeId());
        double tripVoteRating = 0;
        long tripVoteCount = 0;

        if (!tripVoteResponse.isEmpty()) {
            tripVoteRating = tripVoteResponse.getFirst().doubleValue();
            tripVoteCount = tripVoteResponse.getLast().longValue();
        }

        long totalCount = googleCount + tripVoteCount;
        double totalRating = 0;

        if (totalCount != 0) {
            totalRating = (googleRating * googleCount + tripVoteRating * tripVoteCount) / totalCount;
        }

        totalRating = Math.round(totalRating * 10) / 10.0;

        return new ReviewRatingResponse(totalRating, totalCount);
    }


    //Json에서 구글 리뷰+별점을 가져오는 메서드
    public ReviewGetResponse bringJsonReviewInfo(ReviewGetRequest reviewGetRequest, Pageable pageable) {
        Integer placeId = reviewGetRequest.placeId();

        if (pageable.getPageNumber() == 0) {
            var response = ReviewJsonReader.readReviewsJsonFile    //임시로 Json파일에서 읽어옴
                    ("./review-example/reviews/place_" + reviewGetRequest.contentTypeId() + ".json");

            List<Review> reviews = new ArrayList<>(ReviewGetResponse.TestconvertToReviews(response));
            reviews.addAll(reviewRepository.findByPlaceId(placeId, pageable).toList());
            return ReviewGetResponse.from(reviews);
        }
        return ReviewGetResponse.from(reviewRepository.findByPlaceId(placeId, pageable).toList());
    }

    //Json에서 구글 리뷰+별점을 가져오는 메서드
    public ReviewRatingResponse bringJsonReviewRatingAndCount(ReviewGetRequest reviewGetRequest) {
        var googleReviewResponse = ReviewJsonReader.googleTempRatingResponse //임시로 Json파일에서 읽어옴
                ("./review-example/rating/place_rating_" + reviewGetRequest.contentTypeId() + ".json");
        double googleRating = googleReviewResponse.rating();
        long googleCount = googleReviewResponse.userRatingCount();

        List<Number> tripVoteResponse = reviewRepository.countAndAverageRatingByPlaceId(reviewGetRequest.placeId());
        double tripVoteRating = 0;
        long tripVoteCount = 0;

        if (!tripVoteResponse.isEmpty()) {
            tripVoteRating = tripVoteResponse.getFirst().doubleValue();
            tripVoteCount = tripVoteResponse.getLast().longValue();
        }

        long totalCount = googleCount + tripVoteCount;
        double totalRating = 0;

        if (totalCount != 0) {
            totalRating = (googleRating * googleCount + tripVoteRating * tripVoteCount) / totalCount;
        }

        totalRating = Math.round(totalRating * 10) / 10.0;

        return new ReviewRatingResponse(totalRating, totalCount);
    }
}
