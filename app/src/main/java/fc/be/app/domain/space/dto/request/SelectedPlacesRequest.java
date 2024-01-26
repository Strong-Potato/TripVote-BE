package fc.be.app.domain.space.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

public record SelectedPlacesRequest(
        @NotNull
        @Size(min = 1)
        List<SelectedPlaceRequest> places
) {

}
