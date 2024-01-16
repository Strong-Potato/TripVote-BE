package fc.be.app.domain.place.dto;

import fc.be.openapi.tourapi.dto.request.DetailRequest;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record PlaceInfoGetRequest(
        @Positive
        @NotNull
        Integer placeId,

        @Positive
        @NotNull
        Integer placeTypeId
) {
    public DetailRequest toTourAPIRequest() {
        return new DetailRequest(
                placeId,
                placeTypeId
        );
    }
}
