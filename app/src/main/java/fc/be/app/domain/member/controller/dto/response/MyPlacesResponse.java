package fc.be.app.domain.member.controller.dto.response;

import fc.be.app.domain.place.Location;
import fc.be.app.domain.wish.dto.WishGetResponse;

import java.util.List;

public record MyPlacesResponse(
        List<MyPlace> places,
        Integer pageNumber,
        Integer pageSize,
        Integer totalPages,
        Long totalResult,
        boolean first,
        boolean last
) {
    public record MyPlace(
            Long id,
            Integer contentTypeId,
            PlaceInfo location,
            String category,
            String title,
            String thumbnail
    ) {
        public static MyPlace from(WishGetResponse.Item item) {
            return new MyPlace(
                    item.place().id().longValue(),
                    item.place().contentTypeId(),
                    PlaceInfo.from(item.place().location()),
                    item.place().category(),
                    item.place().title(),
                    item.place().thumbnail()
            );
        }

        public record PlaceInfo(
                String address,
                String addressDetail,
                String phone,
                Integer areaCode,
                Integer sigunguCode,
                Integer zipCode,
                Double latitude,
                Double longitude
        ) {
            public static PlaceInfo from(Location location) {
                return new PlaceInfo(
                        location.getAddress(),
                        location.getAddressDetail(),
                        location.getPhone(),
                        location.getAreaCode(),
                        location.getSigunguCode(),
                        location.getZipCode(),
                        location.getLatitude(),
                        location.getLongitude()
                );
            }
        }
    }

    public static MyPlacesResponse from(WishGetResponse wishGetResponse) {
        return new MyPlacesResponse(
                wishGetResponse.wishes().stream().map(MyPlace::from).toList(),
                wishGetResponse.pageNumber(),
                wishGetResponse.pageSize(),
                wishGetResponse.totalPages(),
                wishGetResponse.totalResult(),
                wishGetResponse.first(),
                wishGetResponse.last()
        );
    }
}
