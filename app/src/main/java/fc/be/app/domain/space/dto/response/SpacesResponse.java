package fc.be.app.domain.space.dto.response;

import fc.be.app.domain.space.entity.Space;

import java.util.List;

public record SpacesResponse(
        List<SpaceResponse> spaces,
        Integer pageNumber,
        Integer pageSize,
        Integer totalPages,
        Long totalResult,
        boolean first,
        boolean last
) {
    public static SpacesResponse from(List<Space> spaces) {
        List<SpaceResponse> spaceResponses = spaces.stream()
                .map(SpaceResponse::of)
                .toList();
        return new SpacesResponse(spaceResponses, null, null, null, null, false, false);
    }
}
