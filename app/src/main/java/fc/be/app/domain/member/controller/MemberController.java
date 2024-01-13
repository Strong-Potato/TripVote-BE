package fc.be.app.domain.member.controller;

import fc.be.app.domain.member.dto.response.MyInfoResponse;
import fc.be.app.domain.member.dto.response.MySpaceResponse;
import fc.be.app.domain.member.exception.MemberErrorCode;
import fc.be.app.domain.member.exception.MemberException;
import fc.be.app.domain.member.service.MemberQuery;
import fc.be.app.domain.member.service.MemberQuery.MemberResponse;
import fc.be.app.domain.space.service.SpaceService;
import fc.be.app.domain.space.vo.SpaceType;
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

    @GetMapping("/my-info")
    public ApiResponse<MyInfoResponse> myInfo(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        Long id = userPrincipal.id();
        MemberResponse memberResponse = memberQuery.findById(id)
                .orElseThrow(() -> new MemberException(MemberErrorCode.MEMBER_NOT_FOUND));
        return ApiResponse.ok(MyInfoResponse.from(memberResponse));
    }

    @GetMapping("/my-space")
    public ApiResponse<MySpaceResponse> mySpace(@AuthenticationPrincipal UserPrincipal userPrincipal, Pageable pageable) {
        Long id = userPrincipal.id();
        // TODO: SpaceType의 역할? All도 필요한가?
        MySpaceResponse mySpaceResponse = spaceService.findByEndDateAndMember(LocalDate.now(), id, null, pageable);
        return ApiResponse.ok(mySpaceResponse);
    }

    @GetMapping("/my-space/outdated")
    public ApiResponse<MySpaceResponse> mySpaceOutdated(@AuthenticationPrincipal UserPrincipal userPrincipal, Pageable pageable) {
        Long id = userPrincipal.id();
        MySpaceResponse mySpaceResponse = spaceService.findByEndDateAndMember(LocalDate.now(), id, SpaceType.PAST, pageable);
        return ApiResponse.ok(mySpaceResponse);
    }

    @GetMapping("/my-space/upcoming")
    public ApiResponse<MySpaceResponse> mySpaceUpcoming(@AuthenticationPrincipal UserPrincipal userPrincipal, Pageable pageable) {
        Long id = userPrincipal.id();
        MySpaceResponse mySpaceResponse = spaceService.findByEndDateAndMember(LocalDate.now(), id, SpaceType.UPCOMING, pageable);
        return ApiResponse.ok(mySpaceResponse);
    }
}
