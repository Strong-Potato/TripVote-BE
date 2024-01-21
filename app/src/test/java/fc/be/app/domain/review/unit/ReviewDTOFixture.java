package fc.be.app.domain.review.unit;

import fc.be.app.domain.member.entity.Member;
import fc.be.app.domain.member.vo.AuthProvider;
import fc.be.app.domain.place.Place;
import fc.be.app.domain.review.dto.*;
import fc.be.app.domain.review.entity.Review;
import fc.be.app.global.config.security.model.user.UserPrincipal;
import fc.be.openapi.google.dto.review.APIRatingResponse;
import fc.be.openapi.google.dto.review.APIReviewResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

public interface ReviewDTOFixture extends ReviewFixture {

    default ReviewCreateRequest reviewCreateRequest() {
        return new ReviewCreateRequest(
                (int) property.get("placeId"),
                (int) property.get("contentTypeId"),
                (String) property.get("title"),
                (int) property.get("rating"),
                (String) property.get("content"),
                (List<String>) property.get("images"),
                (LocalDate) property.get("visitedAt")
        );
    }

    default ReviewCreateResponse reviewCreateResponse() {
        return new ReviewCreateResponse(
                (long) property.get("reviewId"),
                (int) property.get("placeId"),
                (int) property.get("rating"),
                (String) property.get("content"),
                (List<String>) property.get("images"),
                (LocalDate) property.get("visitedAt")
        );
    }

    default Member member() {
        return Member.builder()
                .email("ok@gmail.com")
                .nickname("ok")
                .build();
    }

    default Place place() {
        return Place.builder()
                .id((int) property.get("placeId"))
                .title((String) property.get("title"))
                .build();
    }

    default Review review() {
        return Review.builder()
                .place(place())
                .visitedAt(LocalDate.now())
                .content((String) property.get("content"))
                .isGoogle(false)
                .member(member())
                .rating((int) property.get("rating"))
                .build();
    }

    default APIRatingResponse APIRatingResponse() {
        return new APIRatingResponse(
                4.5,
                354
        );
    }

    default APIReviewResponse APIReviewResponse() {
        var items = new APIReviewResponse.APIReviewItem(
                (String) property.get("nickname"),
                (String) property.get("profileImage"),
                (Integer) property.get("rating"),
                (String) property.get("str_visitedAt"),
                (String) property.get("content"),
                (Boolean) property.get("isGoogle"),
                (List<String>) property.get("images")
        );
        return new APIReviewResponse(List.of(items));
    }

    default Page page() {
        return Page.empty(PageRequest.of(0, 10));
    }

    default Pageable pageable() {
        return (PageRequest.of(0, 10));
    }

    default ReviewEditResponse reviewEditResponse() {
        return new ReviewEditResponse(
                (long) property.get("reviewId"),
                (int) property.get("rating"),
                (String) property.get("content"),
                (List<String>) property.get("images"),
                (LocalDate) property.get("visitedAt")
        );
    }

    default ReviewGetRequest reviewGetRequest() {
        return new ReviewGetRequest(
                (int) property.get("placeId"),
                (int) property.get("contentTypeId"),
                (String) property.get("placeTitle")
        );
    }

    default ReviewEditRequest reviewEditRequest() {
        return new ReviewEditRequest(
                (long) property.get("reviewId"),
                (int) property.get("rating"),
                (String) property.get("content"),
                (List<String>) property.get("images"),
                (LocalDate) property.get("visitedAt")
        );
    }

    default UserPrincipal userPrincipal() {
        return new UserPrincipal(
                1L,
                "ok@gmail.com",
                AuthProvider.NONE
        );
    }
}
