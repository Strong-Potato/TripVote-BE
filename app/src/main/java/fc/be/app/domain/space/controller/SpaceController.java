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
import org.springframework.security.access.prepost.PreAuthorize;
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
    @PreAuthorize("isAuthenticated()")
    public ApiResponse<SpaceResponse> createSpace(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        return ApiResponse.created(spaceService.createSpace(userPrincipal.id()));
    }

    @GetMapping("/{spaceId}")
    @PreAuthorize("isAuthenticated()")
    public ApiResponse<SpaceResponse> getSpaceById(@PathVariable Long spaceId, @AuthenticationPrincipal UserPrincipal userPrincipal) {
        return ApiResponse.ok(spaceService.getSpaceById(spaceId, userPrincipal.id()));
    }

    @PutMapping("/{spaceId}/title")
    @PreAuthorize("isAuthenticated()")
    public ApiResponse<SpaceResponse> updateSpaceByTitle(
            @PathVariable Long spaceId,
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @Valid @RequestBody TitleUpdateRequest updateRequest
    ) {
        SpaceResponse spaceResponse = spaceService.updateSpaceByTitle(spaceId, userPrincipal.id(), updateRequest, LocalDate.now());
        return ApiResponse.ok(spaceResponse);
    }

    @PutMapping("/{spaceId}/dates")
    @PreAuthorize("isAuthenticated()")
    public ApiResponse<SpaceResponse> updateSpaceByDates(
            @PathVariable Long spaceId,
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @Valid @RequestBody DateUpdateRequest updateRequest
    ) {
        SpaceResponse spaceResponse = spaceService.updateSpaceByDates(spaceId, userPrincipal.id(), updateRequest, LocalDate.now());
        return ApiResponse.ok(spaceResponse);
    }

    @PutMapping("/{spaceId}/exit")
    @PreAuthorize("isAuthenticated()")
    public ApiResponse<Void> exitSpace(
            @PathVariable Long spaceId,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        spaceService.exitSpace(spaceId, userPrincipal.id());
        return ApiResponse.ok();
    }

    @GetMapping("/{spaceId}/journey")
    @PreAuthorize("isAuthenticated()")
    public ApiResponse<JourneysResponse> getJourneyForSpace(@PathVariable Long spaceId, @AuthenticationPrincipal UserPrincipal userPrincipal) {
        return ApiResponse.ok(spaceService.getJourneyForSpace(spaceId, userPrincipal.id()));
    }

    @PostMapping("/{spaceId}/places")
    @PreAuthorize("isAuthenticated()")
    public ApiResponse<JourneyResponse> selectedPlacesForSpace(
            @PathVariable Long spaceId,
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @Valid @RequestBody SelectedPlaceRequest request
    ) {
        return ApiResponse.ok(spaceService.selectedPlacesForSpace(spaceId, userPrincipal.id(), request, LocalDate.now()));
    }

    @PostMapping("/{spaceId}/places/search")
    @PreAuthorize("isAuthenticated()")
    public ApiResponse<JourneyResponse> selectedPlacesForSpaceSearch(
            @PathVariable Long spaceId,
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @Valid @RequestBody SearchPlacesRequest request
    ) {
        return ApiResponse.ok(spaceService.addSearchedPlacesBySpace(spaceId, userPrincipal.id(), request, LocalDate.now()));
    }

    @PutMapping("/{spaceId}/places")
    @PreAuthorize("isAuthenticated()")
    public ApiResponse<JourneysResponse> updatePlacesForSpace(
            @PathVariable Long spaceId,
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @Valid @RequestBody SelectedPlacesRequest request
    ) {
        return ApiResponse.ok(spaceService.updatePlacesForSpace(spaceId, userPrincipal.id(), request, LocalDate.now()));
    }

    @GetMapping("/recent")
    @PreAuthorize("isAuthenticated()")
    public ApiResponse<SpaceResponse> recentSpace(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        return ApiResponse.ok(spaceTokenService.getRecentSpace(userPrincipal.id()));
    }

    @GetMapping("/city")
    public ApiResponse<CitiesResponse> getCities() {
        return ApiResponse.ok(spaceService.getCities());
    }

    @DeleteMapping("/{spaceId}/places")
    @PreAuthorize("isAuthenticated()")
    public ApiResponse<Void> deleteSelectedPlace(
            @PathVariable Long spaceId,
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @Valid @RequestBody DeletedPlacesRequest request
    ) {
        spaceService.deleteBySelectedPlace(spaceId, userPrincipal.id(), request, LocalDate.now());
        return ApiResponse.ok();
    }
}
