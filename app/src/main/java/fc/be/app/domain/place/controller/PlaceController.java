package fc.be.app.domain.place.controller;

import fc.be.app.domain.place.dto.PlaceInfoGetResponse;
import fc.be.app.domain.place.dto.PlaceInfoInsertRequest;
import fc.be.app.domain.place.dto.PlaceInfoInsertResponse;
import fc.be.app.domain.place.dto.PlaceSearchResponse;
import fc.be.app.domain.place.service.PlaceService;
import fc.be.app.global.http.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/places")
@RestController
@RequiredArgsConstructor
public class PlaceController {
    private final PlaceService placeService;

    @GetMapping("/{placeId}")
    public ApiResponse<PlaceInfoGetResponse> sendPlaceInfo(
            @PathVariable int placeId,
            @RequestParam int contentTypeId
    ) {
        return ApiResponse.ok(
                placeService.bringPlaceInfo(
                        placeId,
                        contentTypeId
                )
        );
    }

    @PostMapping("/{placeId}")
    public ApiResponse<PlaceInfoInsertResponse> savePlaceInfo(
            @PathVariable int placeId,
            @Valid @RequestBody PlaceInfoInsertRequest placeInfoInsertRequest
    ) {
        return ApiResponse.ok(
                placeService.insertPlaceInfo(placeId, placeInfoInsertRequest)
        );
    }

    @GetMapping("/search/{pageNo}")
    public ApiResponse<PlaceSearchResponse> sendSearchKeywordResults(
            @PathVariable int pageNo,
            @RequestParam(defaultValue = "10") int numOfRows,
            @RequestParam(defaultValue = "0") int areaCode,
            @RequestParam(defaultValue = "0") int sigunguCode,
            @RequestParam(defaultValue = "0") int contentTypeId,
            @RequestParam(defaultValue = "_") String keyword,
            @RequestParam(defaultValue = "R") char arrange
    ) {
        return ApiResponse.ok(
                placeService.bringSearchKeywordResults(
                        pageNo, numOfRows,
                        areaCode, sigunguCode,
                        contentTypeId,
                        keyword,
                        arrange
                )
        );
    }
}
