package fc.be.app.domain.space.dto.response;

import fc.be.app.domain.space.entity.Space;
import java.util.List;

public record SpacesResponse(
    List<SpaceResponse> spaceResponses
) {
    public static SpacesResponse from(List<Space> spaces) {
        List<SpaceResponse> spaceResponses = spaces.stream()
            .map(SpaceResponse::of)
            .toList();
        return new SpacesResponse(spaceResponses);
    }
}
