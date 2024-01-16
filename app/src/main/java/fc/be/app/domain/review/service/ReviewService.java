package fc.be.app.domain.review.service;

import fc.be.app.domain.member.entity.Member;
import fc.be.app.domain.member.exception.MemberErrorCode;
import fc.be.app.domain.member.exception.MemberException;
import fc.be.app.domain.member.repository.MemberRepository;
import fc.be.app.domain.place.service.PlaceService;
import fc.be.app.domain.review.dto.*;
import fc.be.app.domain.review.dto.response.ReviewResponse;
import fc.be.app.domain.review.dto.response.ReviewsResponse;
import fc.be.app.domain.review.entity.Review;
import fc.be.app.domain.review.repository.ReviewRepository;
import fc.be.app.global.config.security.model.user.UserPrincipal;
import fc.be.openapi.google.service.ReviewAPIService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final MemberRepository memberRepository;
    private final PlaceService placeService;
    private final ReviewAPIService reviewAPIService;

    @Transactional
    public ReviewCreateResponse createReview(ReviewCreateRequest reviewCreateRequest, UserPrincipal userPrincipal) {

        Integer placeId = reviewCreateRequest.placeId();
        Integer contentTypeId = reviewCreateRequest.contentTypeId();
        Member member = memberRepository.findById(userPrincipal.id()).orElseThrow(() ->
                new MemberException(MemberErrorCode.MEMBER_NOT_FOUND));

        placeService.saveOrUpdatePlace(placeId, contentTypeId);

        return ReviewCreateResponse.from(reviewRepository.save(
                reviewCreateRequest.to(member)));
    }

    @Transactional
    public ReviewEditResponse editReview(ReviewEditRequest reviewEditRequest, UserPrincipal userPrincipal) {
        Review review = reviewRepository.findByIdAndMemberId(reviewEditRequest.reviewId(), userPrincipal.id());
        review.editReview(reviewEditRequest);
        return ReviewEditResponse.from(reviewRepository.save(review));
    }

    public ReviewsResponse getMemberReviews(Long memberId, Pageable pageable) {
        Page<Review> reviews = reviewRepository.findByMemberId(memberId, pageable);

        int totalPages = reviews.getTotalPages();
        long totalElements = reviews.getTotalElements();
        int number = reviews.getNumber();
        int size = reviews.getSize();
        boolean first = reviews.isFirst();
        boolean last = reviews.isLast();
        List<ReviewResponse> content = reviews.get().map(ReviewResponse::of).collect(Collectors.toList());

        return new ReviewsResponse(content, number, size, totalPages, totalElements, first, last);
    }

    @Transactional
    public Integer deleteReview(Long reviewId, UserPrincipal userPrincipal) {
        Long memberId = userPrincipal.id();
        return reviewRepository.deleteByIdAndMemberId(reviewId, memberId);
    }

    public ReviewGetResponse bringJsonReviewInfo(ReviewGetRequest reviewGetRequest, Pageable pageable) {
        Integer placeId = reviewGetRequest.placeId();

        if (pageable.getPageNumber() == 0) {
            var response = reviewAPIService.bringReview
                    (reviewGetRequest.placeTitle(), reviewGetRequest.contentTypeId());

            List<Review> reviews = new ArrayList<>(ReviewGetResponse.convertToReviews(response));
            reviews.addAll(reviewRepository.findByPlaceId(placeId, pageable).toList());
            return ReviewGetResponse.from(reviews);
        }
        return ReviewGetResponse.from(reviewRepository.findByPlaceId(placeId, pageable).toList());
    }

    public ReviewRatingResponse bringJsonReviewRatingAndCount(ReviewGetRequest reviewGetRequest) {
        var googleReviewResponse = reviewAPIService.bringRatingCount
                (reviewGetRequest.placeTitle(), reviewGetRequest.contentTypeId());

        double googleRating = googleReviewResponse.rating();
        long googleCount = googleReviewResponse.userRatingCount();

        List<Number> tripVoteResponse = responseToList(reviewRepository.countAndAverageRatingByPlaceId
                (reviewGetRequest.placeId()));

        double tripVoteRating = tripVoteResponse.getFirst().doubleValue();
        long tripVoteCount = tripVoteResponse.getLast().longValue();

        var calResult = calculateReviewAverage(googleRating, googleCount, tripVoteRating, tripVoteCount);

        return new ReviewRatingResponse(
                calResult.getFirst().doubleValue(),
                calResult.getLast().longValue()
        );
    }

    private List<Number> responseToList(List<Number> response) {
        if (response.isEmpty()) {
            return List.of(0.0, 0L);
        }
        return List.of(response.getFirst().doubleValue(), response.getLast().longValue());
    }

    private List<Number> calculateReviewAverage(double rating1, long count1, double rating2, long count2) {
        long totalCount = count1 + count2;
        double totalRating = 0;

        if (totalCount == 0) {
            return List.of(0.0, 0L);
        }

        totalRating = (rating1 * count1 + rating2 * count2) / totalCount;
        totalRating = Math.round(totalRating * 10) / 10.0;
        return List.of(totalRating, totalCount);
    }
}