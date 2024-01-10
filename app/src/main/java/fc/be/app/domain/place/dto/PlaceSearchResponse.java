package fc.be.app.domain.place.dto;

import fc.be.openapi.tourapi.dto.bone.LocationDTO;
import fc.be.openapi.tourapi.dto.bone.PlaceDTO;
import lombok.Builder;

import java.util.ArrayList;
import java.util.List;

public record PlaceSearchResponse(
        List<Item> places
) {
    public static PlaceSearchResponse from(List<PlaceDTO> places) {
        List<Item> items = new ArrayList<>();

        for (var place : places) {
            items.add(Item.builder()
                    .id(place.getId())
                    .contentTypeId(place.getContentTypeId())
                    .title(place.getTitle())
                    .thumbnail(place.getThumbnail())
                    .location(place.getLocationDTO())
                    .build()
            );
        }
        return new PlaceSearchResponse(items);
    }

    @Builder
    private record Item(
            Integer id,
            Integer contentTypeId,
            String title,
            String thumbnail,
            LocationDTO location
    ) {
    }
}
