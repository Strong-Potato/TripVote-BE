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
    private final MemberRepository memberRepository;


    @Transactional
    public void createReview(Long placeId, String thumbnail, ContentTypeId contentTypeId, String title,
                             Integer areaCode, String content, Integer rating, List<String> images, LocalDate visitedAt) {
        Review review = Review.builder().place()
                .member().content(content).images(images).visitedAt(visitedAt).rating(rating).build();

        reviewRepository.save(review);
    }



    @Transactional
    public Review createReview(ReviewCreateRequest reviewCreateRequest) {

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

    public List<ReviewResponse> getMemberReviews(Long memberId) {
        return null;
    }


    private Class func(ContentTypeId contentTypeId){
        return switch(contentTypeId.getId()){
            case 12 -> Spot.class;
            case 14 -> Facility.class;
            case 15 -> Festival.class;
            case 28 -> Leports.class;
            case 32 -> Accommodation.class;
            case 38 -> Shop.class;
            case 39 -> Restaurant.class;

            default -> throw new ReviewException(ReviewErrorCode.CONTENT_TYPE_NOT_MATCH);
        };
    }
}
