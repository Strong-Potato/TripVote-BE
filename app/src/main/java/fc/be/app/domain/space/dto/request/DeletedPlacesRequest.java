package fc.be.app.domain.space.dto.request;

import java.util.List;

public record DeletedPlacesRequest(
        List<DeletedPlace> places
) {
    public record DeletedPlace(
        Long journeyId,
        List<Long> selectedIds
    ) {

    }
}
