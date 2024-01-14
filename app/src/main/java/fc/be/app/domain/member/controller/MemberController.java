package fc.be.app.domain.member.controller;

import fc.be.app.domain.member.dto.response.MyInfoResponse;
import fc.be.app.domain.member.dto.response.MyPlacesResponse;
import fc.be.app.domain.member.dto.response.MyReviewsResponse;
import fc.be.app.domain.member.dto.response.MySpacesResponse;
import fc.be.app.domain.member.exception.MemberErrorCode;
import fc.be.app.domain.member.exception.MemberException;
import fc.be.app.domain.member.service.MemberQuery;
import fc.be.app.domain.member.service.MemberQuery.MemberResponse;
import fc.be.app.domain.review.dto.response.ReviewsResponse;
import fc.be.app.domain.review.service.ReviewService;
import fc.be.app.domain.space.dto.response.SpacesResponse;
import fc.be.app.domain.space.service.SpaceService;
import fc.be.app.domain.space.vo.SpaceType;
import fc.be.app.domain.wish.dto.WishGetResponse;
import fc.be.app.domain.wish.service.WishService;
import fc.be.app.global.config.security.model.user.UserPrincipal;
import fc.be.app.global.http.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/members")
@RequiredArgsConstructor
public class MemberController {
    private final MemberQuery memberQuery;
    private final SpaceService spaceService;
    private final ReviewService reviewService;
    private final WishService wishService;

    @GetMapping("/my-info")
    public ApiResponse<MyInfoResponse> myInfo(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        Long id = userPrincipal.id();
        MemberResponse memberResponse = memberQuery.findById(id)
                .orElseThrow(() -> new MemberException(MemberErrorCode.MEMBER_NOT_FOUND));
        return ApiResponse.ok(MyInfoResponse.from(memberResponse));
    }

    @GetMapping("/my-spaces")
    public ApiResponse<MySpacesResponse> mySpaces(@AuthenticationPrincipal UserPrincipal userPrincipal, Pageable pageable) {
        Long id = userPrincipal.id();
        // TODO: SpaceType의 역할? All도 필요한가?
        SpacesResponse spacesResponse = spaceService.findByEndDateAndMember(LocalDate.now(), id, null, pageable);
        return ApiResponse.ok(MySpacesResponse.from(spacesResponse));
    }

    @GetMapping("/my-spaces/outdated")
    public ApiResponse<MySpacesResponse> mySpacesOutdated(@AuthenticationPrincipal UserPrincipal userPrincipal, Pageable pageable) {
        Long id = userPrincipal.id();
        SpacesResponse spacesResponse = spaceService.findByEndDateAndMember(LocalDate.now(), id, SpaceType.PAST, pageable);
        return ApiResponse.ok(MySpacesResponse.from(spacesResponse));
    }

    @GetMapping("/my-spaces/upcoming")
    public ApiResponse<MySpacesResponse> mySpacesUpcoming(@AuthenticationPrincipal UserPrincipal userPrincipal, Pageable pageable) {
        Long id = userPrincipal.id();
        SpacesResponse mySpacesResponse = spaceService.findByEndDateAndMember(LocalDate.now(), id, SpaceType.UPCOMING, pageable);
        return ApiResponse.ok(MySpacesResponse.from(mySpacesResponse));
    }

    @GetMapping("/my-reviews")
    public ApiResponse<MySpacesResponse> myReviews(@AuthenticationPrincipal UserPrincipal userPrincipal, Pageable pageable) {
        Long id = userPrincipal.id();
        ReviewsResponse reviewsResponse = reviewService.getMemberReviews(id, pageable);
        return ApiResponse.ok(MyReviewsResponse.from(reviewsResponse));
    }

    @GetMapping("/my-places")
    public ApiResponse<MyPlacesResponse> myPlaces(@AuthenticationPrincipal UserPrincipal userPrincipal, Pageable pageable) {
        Long id = userPrincipal.id();
        WishGetResponse wishesResponse = wishService.getWishes(id, pageable);
        return ApiResponse.ok(MyPlacesResponse.from(wishesResponse));
    }
}
