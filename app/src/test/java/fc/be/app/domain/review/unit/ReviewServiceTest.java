package fc.be.app.domain.review.unit;

import fc.be.app.domain.member.exception.MemberErrorCode;
import fc.be.app.domain.member.exception.MemberException;
import fc.be.app.domain.member.repository.MemberRepository;
import fc.be.app.domain.place.exception.PlaceErrorCode;
import fc.be.app.domain.place.exception.PlaceException;
import fc.be.app.domain.place.service.PlaceService;
import fc.be.app.domain.review.dto.RatingStatisticsDTO;
import fc.be.app.domain.review.dto.ReviewEditResponse;
import fc.be.app.domain.review.entity.Review;
import fc.be.app.domain.review.exception.ReviewErrorCode;
import fc.be.app.domain.review.exception.ReviewException;
import fc.be.app.domain.review.repository.ReviewRepository;
import fc.be.app.domain.review.service.ReviewService;
import fc.be.openapi.google.dto.review.APIRatingResponse;
import fc.be.openapi.google.dto.review.APIReviewResponse;
import fc.be.openapi.google.service.ReviewAPIService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.never;

@ExtendWith(MockitoExtension.class)
public class ReviewServiceTest implements ReviewDTOFixture {

    @InjectMocks
    private ReviewService reviewService;

    @Mock
    private PlaceService placeService;

    @Mock
    private ReviewAPIService reviewAPIService;

    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private MemberRepository memberRepository;

    @Nested
    @DisplayName("리뷰 작성을 수행함")
    class CreateReview {

        @Test
        @DisplayName("인증이 된 사용자는 리뷰를 작성할 수 있다")
        void correctMember_saveRepository() {
            // Given
            given(memberRepository.findById(anyLong())).willReturn(Optional.of((member())));
            given(reviewRepository.save(any(Review.class))).willReturn(review());
            given(placeService.saveOrUpdatePlace(anyInt(), anyInt())).willReturn(restaurant());

            // When
            var actual = reviewService.createReview(reviewCreateRequest(), userPrincipal());

            // Then
            then(reviewRepository).should().save(any());
            assertNotNull(actual);
        }

        @Test
        @DisplayName("인증이 되지 않은 사용자는 리뷰를 작성할 수 없다")
        void unauthenticatedUserCannotWriteReview() {
            // Given
            given(memberRepository.findById(anyLong())).willThrow(new MemberException(MemberErrorCode.MEMBER_NOT_FOUND));

            // When
            assertThrows(MemberException.class,
                    () -> reviewService.createReview(reviewCreateRequest(), userPrincipal()));

            // Then
            then(reviewRepository).should(never()).save(any());
            then(placeService).should(never()).saveOrUpdatePlace
                    ((int) property.get("placeId"), (int) property.get("contentTypeId"));
        }

        @Test
        @DisplayName("Tour API에 없는 여행 정보에 대한 리뷰는 작성할 수 없다.")
        void cannotWriteReviewForNonExistingPlaceInfo() {
            // Given
            given(memberRepository.findById(anyLong())).willReturn(Optional.of((member())));
            given(placeService.saveOrUpdatePlace(anyInt(), anyInt())).willThrow(new PlaceException(PlaceErrorCode.PLACE_NOT_LOADED));

            // When & Then
            assertThrows(PlaceException.class, () -> reviewService.createReview(reviewCreateRequest(), userPrincipal()));
            then(reviewRepository).should(never()).save(any());
        }
    }

    @Nested
    @DisplayName("리뷰를 수정을 수행함")
    class EditReview {

        @Test
        @DisplayName("인증이 된 사용자는 리뷰를 수정할 수 있다")
        void correctMember_updateRepository() {
            // Given
            given(reviewRepository.findByIdAndMemberId(anyLong(), anyLong())).willReturn(review());
            given(reviewRepository.save(any(Review.class))).willReturn(review());

            try (var reviewEditResponse = mockStatic(ReviewEditResponse.class)) {
                reviewEditResponse.when(() -> ReviewEditResponse.from(any())).thenReturn(reviewEditResponse());
            }

            // When
            var actual = reviewService.editReview(reviewEditRequest(), userPrincipal());

            // Then
            then(reviewRepository).should().save(any());
            assertNotNull(actual);
        }

        @Test
        @DisplayName("인증이 되지 않은 사용자는 리뷰를 수정할 수 없다")
        void unauthenticatedUserCannotEditReview() {
            // Given
            given(reviewRepository.findByIdAndMemberId(anyLong(), anyLong()))
                    .willThrow(new MemberException(MemberErrorCode.MEMBER_NOT_FOUND));

            // When & Then
            assertThrows(MemberException.class,
                    () -> reviewRepository.findByIdAndMemberId(anyLong(), anyLong()));
            then(reviewRepository).should(never()).save(any());
        }

        @Test
        @DisplayName("수정하려는 리뷰가 존재하지 않는 경우 예외가 발생한다.")
        void shouldThrowExceptionWhenReviewDoesNotExistDuringModification() {
            // Given
            given(reviewRepository.findByIdAndMemberId(anyLong(), anyLong()))
                    .willThrow(new ReviewException(ReviewErrorCode.REVIEW_NOT_FOUND));

            // When & Then
            assertThrows(ReviewException.class,
                    () -> reviewRepository.findByIdAndMemberId(anyLong(), anyLong()));
            then(reviewRepository).should(never()).save(any());
        }
    }

    @Nested
    @DisplayName("리뷰를 삭제를 수행함")
    class DeleteReview {

        @Test
        @DisplayName("인증이 된 사용자는 리뷰를 삭제할 수 있다.")
        void shouldAllowAuthenticatedUserToDeleteReview() {
            Long reviewId = (long) property.get("reviewId");
            Long memberId = userPrincipal().id();
            // Given
            given(reviewRepository.deleteByIdAndMemberId(eq(reviewId), eq(memberId))).willReturn(1);

            // When
            Integer actual = reviewService.deleteReview(reviewId, userPrincipal());

            // Then
            then(reviewRepository).should().deleteByIdAndMemberId(eq(reviewId), eq(memberId));
            assertNotNull(actual);
        }

        @Test
        @DisplayName("리뷰가 삭제되지 않은 경우 0을 반환한다.")
        void shouldReturnZeroWhenReviewNotDeleted() {
            Long reviewId = (long) property.get("reviewId");
            Long memberId = userPrincipal().id();
            // Given
            given(reviewRepository.deleteByIdAndMemberId(eq(reviewId), eq(memberId))).willReturn(0);

            // When
            Integer actual = reviewService.deleteReview(reviewId, userPrincipal());

            // Then
            then(reviewRepository).should().deleteByIdAndMemberId(eq(reviewId), eq(memberId));
            assertNotNull(actual);
        }

        @Test
        @DisplayName("인증이 되지 않은 사용자는 리뷰를 삭제할 수 없다.")
        void shouldPreventUnauthenticatedUserFromDeletingReview() {
            //Given
            given(reviewRepository.deleteByIdAndMemberId(any(), any()))
                    .willThrow(new MemberException(MemberErrorCode.MEMBER_NOT_FOUND));

            // When & Then
            assertThrows(MemberException.class,
                    () -> reviewRepository.deleteByIdAndMemberId(anyLong(), anyLong()));
            then(reviewRepository).should(never()).save(any());
        }
    }

    @Nested
    @DisplayName("리뷰를 조회를 수행함")
    class ReadReview {

        @Test
        @DisplayName("TourAPI로 부터 가져온 Place에 대한 리뷰를 조회할 수 있다.")
        void shouldRetrieveReviewsForPlaceFromTourAPI() {
            // Given
            var reviewGetRequest = reviewGetRequest();
            // Mock behavior of reviewAPIService
            given(reviewAPIService.bringReview(reviewGetRequest.placeTitle(), reviewGetRequest.contentTypeId()))
                    .willReturn(APIReviewResponse());
            // Mock behavior of reviewRepository
            given(reviewRepository.findByPlaceId(reviewGetRequest.placeId(), pageable()))
                    .willReturn(page());

            // When
            var actual = reviewService.bringReviewInfo(reviewGetRequest, pageable());

            // Then
            then(reviewAPIService).should().bringReview
                    (reviewGetRequest.placeTitle(), reviewGetRequest.contentTypeId());
            then(reviewRepository).should().findByPlaceId(any(), any());
            assertNotNull(actual);
        }

        @Test
        @DisplayName("특정 Place에 대한 평점과 총 리뷰의 개수를 조회할 수 있다.")
        void shouldRetrieveRatingAndTotalReviewCountForSpecificPlace() {
            // Given
            var reviewGetRequest = reviewGetRequest();
            var APIRatingResponse = APIRatingResponse();
            // Mock behavior of reviewAPIService
            given(reviewAPIService.bringRatingCount(reviewGetRequest.placeTitle(), reviewGetRequest.contentTypeId()))
                    .willReturn(APIRatingResponse);
            // Mock behavior of reviewRepository
            given(reviewRepository.findRatingStatisticsByPlaceId(reviewGetRequest.placeId()))
                    .willReturn(new RatingStatisticsDTO(APIRatingResponse.rating(),
                            APIRatingResponse.userRatingCount().longValue()));

            // When
            var actual = reviewService.bringReviewRatingAndCount(reviewGetRequest);

            // Then
            then(reviewAPIService).should().bringRatingCount
                    (reviewGetRequest.placeTitle(), reviewGetRequest.contentTypeId());
            then(reviewRepository).should().findRatingStatisticsByPlaceId(any());
            assertNotNull(actual);
        }

        @Test
        @DisplayName("요청한 Place에 대한 리뷰가 없는 경우 빈 리스트를 반환한다.")
        void shouldReturnEmptyListWhenNoReviewsForRequestedPlace() {
            // Given
            var reviewGetRequest = reviewGetRequest();
            // Mock behavior of reviewAPIService
            given(reviewAPIService.bringReview(reviewGetRequest.placeTitle(), reviewGetRequest.contentTypeId()))
                    .willReturn(new APIReviewResponse(List.of()));
            // Mock behavior of reviewRepository
            given(reviewRepository.findByPlaceId(reviewGetRequest.placeId(), pageable()))
                    .willReturn(page());

            // When
            var actual = reviewService.bringReviewInfo(reviewGetRequest, pageable());

            // Then
            then(reviewAPIService).should().bringReview
                    (reviewGetRequest.placeTitle(), reviewGetRequest.contentTypeId());
            then(reviewRepository).should().findByPlaceId(any(), any());
            assertTrue(actual.reviews().isEmpty());
        }

        @Test
        @DisplayName("리뷰가 없는 Place에 대한 별점 및 리뷰 수를 조회한 경우 (0.0, 0)리스트를 반환한다.")
        void shouldReturnZeroValueListForPlaceWithNoReviews() {
            // Given
            var reviewGetRequest = reviewGetRequest();
            // Mock behavior of reviewAPIService
            given(reviewAPIService.bringRatingCount(reviewGetRequest.placeTitle(), reviewGetRequest.contentTypeId()))
                    .willReturn(new APIRatingResponse(0.0, 0));
            // Mock behavior of reviewRepository
            given(reviewRepository.findRatingStatisticsByPlaceId(reviewGetRequest.placeId()))
                    .willReturn(new RatingStatisticsDTO(
                            0.0, 0L));

            // When
            var actual = reviewService.bringReviewRatingAndCount(reviewGetRequest);

            // Then
            then(reviewAPIService).should().bringRatingCount
                    (reviewGetRequest.placeTitle(), reviewGetRequest.contentTypeId());
            then(reviewRepository).should().findRatingStatisticsByPlaceId(any());
            assertEquals(0.0, actual.rating());
            assertEquals(0, actual.userRatingCount());
        }
    }
}


