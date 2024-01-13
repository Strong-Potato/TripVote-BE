package fc.be.app.domain.space.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public record SelectedPlaceRequest(
    @NotNull
    Long journeyId,
    @NotEmpty
    List<Integer> selectedPlaces
) {

}
