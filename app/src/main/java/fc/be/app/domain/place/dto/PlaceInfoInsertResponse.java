package fc.be.app.domain.place.dto;

import fc.be.app.domain.place.Place;
import fc.be.openapi.tourapi.dto.bone.LocationDTO;

public record PlaceInfoInsertResponse(
        Integer id,
        Integer contentTypeId,
        String title,
        LocationDTO location,
        String thumbnail
) {
    public static PlaceInfoInsertResponse from(Place place) {
        return new PlaceInfoInsertResponse(
                place.getId(),
                place.getContentTypeId().getId(),
                place.getTitle(),
                LocationDTO.builder()
                        .areaCode(place.getLocation().getAreaCode())
                        .sigunguCode(place.getLocation().getSigunguCode())
                        .build(),
                place.getThumbnail()
        );
    }
}
