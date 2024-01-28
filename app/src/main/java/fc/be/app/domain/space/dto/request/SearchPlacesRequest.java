package fc.be.app.domain.space.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.List;

public record SearchPlacesRequest(
        @Positive
        @NotNull
        Long journeyId,
        @NotEmpty
        List<SearchPlace> places
) {

    public record SearchPlace(Integer placeId, Integer contentTypeId) {
    }

}
