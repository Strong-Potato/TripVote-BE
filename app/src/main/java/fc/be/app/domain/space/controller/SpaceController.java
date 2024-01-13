package fc.be.app.domain.space.controller;

import fc.be.app.domain.space.dto.request.DateUpdateRequest;
import fc.be.app.domain.space.dto.request.SelectedPlaceRequest;
import fc.be.app.domain.space.dto.request.SelectedPlacesRequest;
import fc.be.app.domain.space.dto.request.TitleUpdateRequest;
import fc.be.app.domain.space.dto.response.JourneyResponse;
import fc.be.app.domain.space.dto.response.JourneysResponse;
import fc.be.app.domain.space.dto.response.SpaceResponse;
import fc.be.app.domain.space.dto.response.SpacesResponse;
import fc.be.app.domain.space.service.SpaceService;
import fc.be.app.domain.space.vo.SpaceType;
import fc.be.app.global.http.ApiResponse;
import jakarta.validation.Valid;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/spaces")
public class SpaceController {

    private final SpaceService spaceService;

    @PostMapping
    public ApiResponse<SpaceResponse> createSpace() {
        // todo 로그인 구현 시 @AuthenticationPrincipal 어노테이션 등으로 변경할 예정
        Long memberId = 1L;
        return ApiResponse.created(spaceService.createSpace(memberId));
    }

    @GetMapping("/{spaceId}")
    public ApiResponse<SpaceResponse> getSpaceById(@PathVariable Long spaceId) {
        return ApiResponse.ok(spaceService.getSpaceById(spaceId));
    }

    @PutMapping("/{spaceId}/title")
    public ApiResponse<SpaceResponse> updateSpaceByTitle(@PathVariable Long spaceId,
        @Valid @RequestBody TitleUpdateRequest updateRequest) {
        SpaceResponse spaceResponse = spaceService.updateSpaceByTitle(spaceId, updateRequest);
        return ApiResponse.ok(spaceResponse);
    }

    @PutMapping("/{spaceId}/dates")
    public ApiResponse<SpaceResponse> updateSpaceByDates(@PathVariable Long spaceId,
        @Valid @RequestBody DateUpdateRequest updateRequest) {
        SpaceResponse spaceResponse = spaceService.updateSpaceByDates(spaceId, updateRequest);
        return ApiResponse.ok(spaceResponse);
    }

    @GetMapping
    public ApiResponse<SpacesResponse> getSpaceListWithType(@RequestParam SpaceType type) {
        // todo 로그인 구현 시 @AuthenticationPrincipal 어노테이션 등으로 변경할 예정
        Long memberId = 1L;
        return ApiResponse.ok(spaceService.findByEndDateAndMember(LocalDate.now(),
            memberId, type));
    }

    @PutMapping("/{spaceId}/exit")
    public ApiResponse<Void> exitSpace(@PathVariable Long spaceId) {
        // todo 로그인 구현 시 @AuthenticationPrincipal 어노테이션 등으로 변경할 예정
        Long memberId = 1L;
        spaceService.exitSpace(spaceId, memberId);
        return ApiResponse.ok();
    }

    @GetMapping("/{spaceId}/journey")
    public ApiResponse<JourneysResponse> getJourneyForSpace(@PathVariable Long spaceId) {
        return ApiResponse.ok(spaceService.getJourneyForSpace(spaceId));
    }

    @PostMapping("/select-places")
    public ApiResponse<JourneyResponse> selectedPlacesForSpace(@Valid @RequestBody SelectedPlaceRequest request) {
        return ApiResponse.ok(spaceService.selectedPlacesForSpace(request));
    }

    @PutMapping("/select-places")
    public ApiResponse<JourneysResponse> updatePlacesForSpace(@Valid @RequestBody SelectedPlacesRequest request) {
        return ApiResponse.ok(spaceService.updatePlacesForSpace(request));
    }

}
