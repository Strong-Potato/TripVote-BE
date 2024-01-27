package fc.be.app.domain.space.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.util.List;

public record DeletedPlacesRequest(
        @NotNull @Size(min = 1) @Valid
        List<DeletedPlace> places
) {
    public record DeletedPlace(
            @Positive
            @NotNull
            Long journeyId,

            @NotNull
            @Size(min = 1)
            List<Long> selectedIds
    ) {

    }
}
