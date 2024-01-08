package fc.be.app.domain.place.dto;

import fc.be.openapi.tourapi.dto.bone.PlaceDTO;
import lombok.Builder;

import java.util.ArrayList;
import java.util.List;

public record PlaceNearbyResponse(
        List<PlaceNearbyItem> places
) {

    public static PlaceNearbyResponse from(List<? extends PlaceDTO> places) {
        List<PlaceNearbyItem> items = new ArrayList<>();
        for (var place : places) {
            items.add(PlaceNearbyItem.builder()
                    .id(place.getId())
                    .contentTypeId(place.getContentTypeId())
                    .title(place.getTitle())
                    .thumbnail(place.getThumbnail())
                    .areaCode(place.getLocationDTO().getAreaCode())
                    .sigunguCode(place.getLocationDTO().getSigunguCode())
                    .rate(0.0)
                    .reviewCount(0L)
                    .build()
            );
        }
        return new PlaceNearbyResponse(items);
    }

    @Builder
    private record PlaceNearbyItem(
            Integer id, Integer contentTypeId,
            String title, String thumbnail,
            Integer areaCode, Integer sigunguCode,
            Double rate, Long reviewCount
    ) {
    }
}
