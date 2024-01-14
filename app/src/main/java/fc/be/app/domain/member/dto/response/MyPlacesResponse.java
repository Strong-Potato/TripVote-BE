package fc.be.app.domain.member.dto.response;

import fc.be.app.domain.wish.dto.WishGetResponse;
import fc.be.openapi.tourapi.tools.area.AreaFinder;

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
            String area,
            String category,
            String title,
            String thumbnail
    ) {
        public static MyPlace from(WishGetResponse.Item item) {
            return new MyPlace(
                    item.place().id().longValue(),
                    AreaFinder.getCityName(item.place().location().getAreaCode()),
                    item.place().category(),
                    item.place().title(),
                    item.place().thumbnail()
            );
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
