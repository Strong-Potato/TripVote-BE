package fc.be.app.domain.place.dto;

import fc.be.openapi.tourapi.dto.request.AreaBasedSync1Request;
import jakarta.validation.constraints.PositiveOrZero;

import java.util.Optional;

public record PlaceNearbyRequest(
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
        Character sort,
        String categoryCode
) {
    public PlaceNearbyRequest {
        page = Optional.ofNullable(page).orElse(1);
        size = Optional.ofNullable(size).orElse(10);
        areaCode = Optional.ofNullable(areaCode).orElse(0);
        sigunguCode = Optional.ofNullable(sigunguCode).orElse(0);
        placeTypeId = Optional.ofNullable(placeTypeId).orElse(0);
        sort = Optional.ofNullable(sort).orElse('R');
    }

    public AreaBasedSync1Request toTourAPIRequest() {
        return new AreaBasedSync1Request(
                page,
                size,
                areaCode,
                sigunguCode,
                placeTypeId,
                sort,
                categoryCode
        );
    }
}