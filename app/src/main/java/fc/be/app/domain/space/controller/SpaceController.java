package fc.be.app.domain.space.controller;

import fc.be.app.domain.space.dto.request.*;
import fc.be.app.domain.space.dto.response.CitiesResponse;
import fc.be.app.domain.space.dto.response.JourneyResponse;
import fc.be.app.domain.space.dto.response.JourneysResponse;
import fc.be.app.domain.space.dto.response.SpaceResponse;
import fc.be.app.domain.space.service.SpaceService;
import fc.be.app.domain.space.service.SpaceTokenService;
import fc.be.app.global.config.security.model.user.UserPrincipal;
import fc.be.app.global.http.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/spaces")
public class SpaceController {

    private final SpaceService spaceService;
    private final SpaceTokenService spaceTokenService;

    @PostMapping
    public ApiResponse<SpaceResponse> createSpace(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        return ApiResponse.created(spaceService.createSpace(userPrincipal.id()));
    }

    @GetMapping("/{spaceId}")
    public ApiResponse<SpaceResponse> getSpaceById(@PathVariable Long spaceId, @AuthenticationPrincipal UserPrincipal userPrincipal) {
        return ApiResponse.ok(spaceService.getSpaceById(spaceId, userPrincipal.id()));
    }

    @PutMapping("/{spaceId}/title")
    public ApiResponse<SpaceResponse> updateSpaceByTitle(
            @PathVariable Long spaceId,
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @Valid @RequestBody TitleUpdateRequest updateRequest
    ) {
        SpaceResponse spaceResponse = spaceService.updateSpaceByTitle(spaceId, userPrincipal.id(), updateRequest, LocalDate.now());
        return ApiResponse.ok(spaceResponse);
    }

    @PutMapping("/{spaceId}/dates")
    public ApiResponse<SpaceResponse> updateSpaceByDates(
            @PathVariable Long spaceId,
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @Valid @RequestBody DateUpdateRequest updateRequest
    ) {
        SpaceResponse spaceResponse = spaceService.updateSpaceByDates(spaceId, userPrincipal.id(), updateRequest, LocalDate.now());
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
    public ApiResponse<JourneysResponse> getJourneyForSpace(@PathVariable Long spaceId, @AuthenticationPrincipal UserPrincipal userPrincipal) {
        return ApiResponse.ok(spaceService.getJourneyForSpace(spaceId, userPrincipal.id()));
    }

    @PostMapping("/{spaceId}/places")
    public ApiResponse<JourneyResponse> selectedPlacesForSpace(
            @PathVariable Long spaceId,
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @Valid @RequestBody SelectedPlaceRequest request
    ) {
        return ApiResponse.ok(spaceService.selectedPlacesForSpace(spaceId, userPrincipal.id(), request, LocalDate.now()));
    }

    @PutMapping("/{spaceId}/places")
    public ApiResponse<JourneysResponse> updatePlacesForSpace(
            @PathVariable Long spaceId,
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @Valid @RequestBody SelectedPlacesRequest request
    ) {
        return ApiResponse.ok(spaceService.updatePlacesForSpace(spaceId, userPrincipal.id(), request, LocalDate.now()));
    }

    @GetMapping("/recent")
    public ApiResponse<SpaceResponse> recentSpace(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        return ApiResponse.ok(spaceTokenService.getRecentSpace(userPrincipal.id()));
    }

    @GetMapping("/city")
    public ApiResponse<CitiesResponse> getCities() {
        return ApiResponse.ok(spaceService.getCities());
    }

    @DeleteMapping("/{spaceId}/places")
    public ApiResponse<Void> deleteSelectedPlace(
            @PathVariable Long spaceId,
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @RequestBody DeletedPlacesRequest request
    ) {
        spaceService.deleteBySelectedPlace(spaceId, userPrincipal.id(), request, LocalDate.now());
        return ApiResponse.ok();
    }
}
