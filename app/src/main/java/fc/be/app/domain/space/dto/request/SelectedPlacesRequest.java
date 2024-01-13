package fc.be.app.domain.space.dto.request;

import java.util.List;

public record SelectedPlacesRequest(
        List<SelectedPlaceRequest> selectedPlaceRequests
) {

}
