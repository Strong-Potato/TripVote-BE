package fc.be.app.domain.place.dto;

import fc.be.openapi.tourapi.dto.bone.PlaceDTO;
import lombok.Builder;

import java.util.ArrayList;
import java.util.List;

public record PlaceSearchResponse(
        List<PlaceSearshItem> places
) {
    public static PlaceSearchResponse from(List<? extends PlaceDTO> places) {
        List<PlaceSearshItem> items = new ArrayList<>();

        // todo : 별점 총점(DB 내부 개수 + Google 내부 개수) 집계 필요
        for (var place : places) {
            items.add(PlaceSearshItem.builder()
                    .id(place.getId())
                    .contentTypeId(place.getContentTypeId())
                    .title(place.getTitle())
                    .thumbnail(place.getThumbnail())
                    .areaCode(place.getLocationDTO().getAreaCode())
                    .sigunguCode(place.getLocationDTO().getSigunguCode())
                    .latitude(place.getLocationDTO().getLatitude())
                    .longitude(place.getLocationDTO().getLongitude())
                    .rate(0.0)
                    .reviewCount(0L)
                    .build()
            );
        }
        return new PlaceSearchResponse(items);
    }

    @Builder
    private record PlaceSearshItem(
            Integer id, Integer contentTypeId,
            String title, String thumbnail,
            Integer areaCode, Integer sigunguCode,
            Double latitude, Double longitude,
            Double rate, Long reviewCount
    ) {
    }
}
