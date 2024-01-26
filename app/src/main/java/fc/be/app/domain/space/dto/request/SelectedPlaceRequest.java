package fc.be.app.domain.space.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.List;

public record SelectedPlaceRequest(
        @Positive
        @NotNull
        Long journeyId,
        /*
        place entity id 값
         */
        @NotEmpty
        List<Integer> placeIds
) {

}
