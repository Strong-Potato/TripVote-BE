package fc.be.app.domain.space.dto.request;

import jakarta.validation.constraints.*;

import java.util.List;

public record SelectedPlaceRequest(
        @Positive
        Long journeyId,
        @NotEmpty
        List<Integer> selectedPlaces
) {

}
