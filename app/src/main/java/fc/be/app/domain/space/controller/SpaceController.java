package fc.be.app.domain.space.controller;

import fc.be.app.domain.space.dto.request.DateUpdateRequest;
import fc.be.app.domain.space.dto.request.SelectedPlaceRequest;
import fc.be.app.domain.space.dto.request.SelectedPlacesRequest;
import fc.be.app.domain.space.dto.request.TitleUpdateRequest;
import fc.be.app.domain.space.dto.response.JourneyResponse;
import fc.be.app.domain.space.dto.response.JourneysResponse;
import fc.be.app.domain.space.dto.response.SpaceResponse;
import fc.be.app.domain.space.service.SpaceService;
import fc.be.app.global.config.security.model.user.UserPrincipal;
import fc.be.app.global.http.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/spaces")
public class SpaceController {

    private final SpaceService spaceService;

    @PostMapping
    public ApiResponse<SpaceResponse> createSpace(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        return ApiResponse.created(spaceService.createSpace(userPrincipal.id()));
    }

    @GetMapping("/{spaceId}")
    public ApiResponse<SpaceResponse> getSpaceById(@PathVariable Long spaceId) {
        return ApiResponse.ok(spaceService.getSpaceById(spaceId));
    }

    @PutMapping("/{spaceId}/title")
    public ApiResponse<SpaceResponse> updateSpaceByTitle(
            @PathVariable Long spaceId,
            @Valid @RequestBody TitleUpdateRequest updateRequest
    ) {
        SpaceResponse spaceResponse = spaceService.updateSpaceByTitle(spaceId, updateRequest);
        return ApiResponse.ok(spaceResponse);
    }

    @PutMapping("/{spaceId}/dates")
    public ApiResponse<SpaceResponse> updateSpaceByDates(
            @PathVariable Long spaceId,
            @Valid @RequestBody DateUpdateRequest updateRequest
    ) {
        SpaceResponse spaceResponse = spaceService.updateSpaceByDates(spaceId, updateRequest);
        return ApiResponse.ok(spaceResponse);
    }

    @PutMapping("/{spaceId}/exit")
    public ApiResponse<Void> exitSpace(
            @PathVariable Long spaceId,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        spaceService.exitSpace(spaceId, userPrincipal.id());
        return ApiResponse.ok();
    }

    @GetMapping("/{spaceId}/journey")
    public ApiResponse<JourneysResponse> getJourneyForSpace(@PathVariable Long spaceId) {
        return ApiResponse.ok(spaceService.getJourneyForSpace(spaceId));
    }

    @PostMapping("/{spaceId}/places")
    public ApiResponse<JourneyResponse> selectedPlacesForSpace(
            @PathVariable Long spaceId,
            @Valid @RequestBody SelectedPlaceRequest request
    ) {
        return ApiResponse.ok(spaceService.selectedPlacesForSpace(request));
    }

    @PutMapping("/{spaceId}/places")
    public ApiResponse<JourneysResponse> updatePlacesForSpace(
            @PathVariable Long spaceId,
            @Valid @RequestBody SelectedPlacesRequest request
    ) {
        return ApiResponse.ok(spaceService.updatePlacesForSpace(request));
    }

    @GetMapping
    public ApiResponse<SpaceResponse> getLatestSpace() {
        // todo API 명세를 위한 껍데기이며 최근 작업한 여행스페이스 검색로직 추가 예정
        return ApiResponse.ok(SpaceResponse.builder().build());
    }
}
