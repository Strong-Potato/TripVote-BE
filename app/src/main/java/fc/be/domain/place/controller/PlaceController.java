package fc.be.domain.place.controller;

import fc.be.domain.place.service.PlaceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class PlaceController {
    private final PlaceService placeService;

    @GetMapping("/place/{placeId}")
    public ResponseEntity<?> sendPLaceInfo(
            @PathVariable int placeId,
            @RequestParam int contentTypeId
    ) {

        return ResponseEntity.ok(
                placeService.bringPlaceInfo(
                        placeId,
                        contentTypeId
                )
        );
    }
}
