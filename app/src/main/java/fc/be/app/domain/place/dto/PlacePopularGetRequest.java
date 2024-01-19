package fc.be.app.domain.place.dto;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

import java.util.Optional;

public record PlacePopularGetRequest(
        @PositiveOrZero
        Integer size,
        @Positive
        Integer placeTypeId
) {

    public PlacePopularGetRequest {
        size = Optional.ofNullable(size).orElse(10);
    }
}
