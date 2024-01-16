package fc.be.app.domain.place.dto;

import fc.be.openapi.tourapi.dto.request.SearchKeyword1Request;
import jakarta.validation.constraints.PositiveOrZero;

import java.util.Optional;

public record PlaceSearchRequest(
        @PositiveOrZero
        Integer page,
        @PositiveOrZero
        Integer size,
        @PositiveOrZero
        Integer areaCode,
        @PositiveOrZero
        Integer sigunguCode,
        @PositiveOrZero
        Integer placeTypeId,
        String keyword,
        Character sort,
        String categoryCode
) {
    public PlaceSearchRequest {
        page = Optional.ofNullable(page).orElse(1);
        size = Optional.ofNullable(size).orElse(10);
        areaCode = Optional.ofNullable(areaCode).orElse(0);
        sigunguCode = Optional.ofNullable(sigunguCode).orElse(0);
        placeTypeId = Optional.ofNullable(placeTypeId).orElse(0);
        keyword = Optional.ofNullable(keyword).orElse("_");
        sort = Optional.ofNullable(sort).orElse('R');
    }

    public SearchKeyword1Request toTourAPIRequest() {
        return new SearchKeyword1Request(
                page,
                size,
                areaCode,
                sigunguCode,
                placeTypeId,
                keyword,
                sort,
                categoryCode
        );
    }
}