package fc.be.app.domain.place.dto;

import fc.be.app.domain.place.Location;
import fc.be.app.domain.place.Place;
import fc.be.openapi.tourapi.constant.ContentTypeId;
import fc.be.openapi.tourapi.dto.bone.LocationDTO;

public record PlaceInfoInsertRequest(
        Integer contentTypeId,
        String title,
        LocationDTO location,
        String thumbnail
) {
    public Place to(int id) {
        return Place.builder()
                .id(id)
                .contentTypeId(ContentTypeId.of(contentTypeId))
                .title(title)
                .location(Location.builder()
                        .areaCode(location.getAreaCode())
                        .sigunguCode(location.getSigunguCode())
                        .build()
                )
                .thumbnail(thumbnail)
                .build();
    }
}
