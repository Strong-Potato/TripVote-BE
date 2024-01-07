package fc.be.app.domain.place.controller;

import fc.be.app.domain.place.dto.PlaceInfoGetResponse;
import fc.be.app.domain.place.dto.PlaceInfoInsertRequest;
import fc.be.app.domain.place.dto.PlaceInfoInsertResponse;
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
}
