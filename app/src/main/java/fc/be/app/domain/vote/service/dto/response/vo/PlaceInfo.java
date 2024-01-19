package fc.be.app.domain.vote.service.dto.response.vo;

import fc.be.app.domain.place.Place;

public record PlaceInfo(
        Integer placeId,
        String placeName,
        String category,
        String placeImageUrl,
        LatLng latLng
) {

    public static PlaceInfo of(Place place) {
        return new PlaceInfo(
                place.getId(),
                place.getThumbnail(),
                place.getCategory(),
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
