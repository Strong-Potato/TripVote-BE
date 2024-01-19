package fc.be.app.domain.place.controller;

import fc.be.app.domain.place.dto.*;
import fc.be.app.domain.place.service.PlaceService;
import fc.be.app.global.http.ApiResponse;
import fc.be.openapi.algolia.SearchEngineService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/places")
@RestController
@RequiredArgsConstructor
public class PlaceController {
    private final PlaceService placeService;
    private final SearchEngineService searchEngineService;

    @GetMapping("/info")
    public ApiResponse<PlaceInfoGetResponse> sendPlaceInfo(
            @Valid @ModelAttribute PlaceInfoGetRequest placeInfoGetRequest
    ) {
        return ApiResponse.ok(placeService.bringPlaceInfo(placeInfoGetRequest));
    }

    @GetMapping("/search")
    public ApiResponse<PlaceSearchResponse> sendSearchKeywordResults(
            @Valid @ModelAttribute PlaceSearchRequest placeSearchRequest
    ) {
        final String wildCard = "_";
        if (!placeSearchRequest.keyword().equals(wildCard)) {
            searchEngineService.saveSearchHistory(placeSearchRequest.keyword());
        }
        return ApiResponse.ok(placeService.bringSearchKeywordResults(placeSearchRequest));
    }

    @GetMapping("/nearby")
    public ApiResponse<PlaceNearbyResponse> sendNearbyPlaces(
            @Valid @ModelAttribute PlaceNearbyRequest placeNearbyRequest
    ) {
        return ApiResponse.ok(placeService.bringNearbyPlaces(placeNearbyRequest));
    }

    @GetMapping("/popular")
    public ApiResponse<PlacePopularGetResponse> sendPopularPlaces(
            @Valid @ModelAttribute PlacePopularGetRequest placePopularGetRequest
    ) {
        return ApiResponse.ok(placeService.bringPopularPlaces(placePopularGetRequest));
    }

    @GetMapping("/popular/keywords")
    public ApiResponse<List<String>> sendPopularSearchKeywords(@RequestParam(defaultValue = "10") Integer size) {
        return ApiResponse.ok(searchEngineService.bringPopularSearchKeywords(size));
    }
}
