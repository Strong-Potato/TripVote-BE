package fc.be.app.domain.vote.service.dto.response.vo;

import fc.be.app.domain.place.Place;

public record PlaceInfo(
        Integer placeId,
        Integer contentTypeId,
        String placeName,
        String category,
        int areaCode,
        String placeImageUrl,
        LatLng latLng
) {

    public static PlaceInfo of(Place place) {
        return new PlaceInfo(
                place.getId(),
                place.getContentTypeId().getId(),
                place.getTitle(),
                place.getCategory(),
                place.getLocation().getAreaCode(),
                place.getThumbnail(),
                new LatLng(
                        place.getLocation().getLatitude(),
                        place.getLocation().getLongitude()
                )
        );
    }

    public record LatLng(
            double lat,
            double lng
    ) {

    }
}
