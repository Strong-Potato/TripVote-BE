package fc.be.app.domain.member.dto.response;

import fc.be.app.domain.space.dto.response.SpaceResponse;

import java.util.List;

public record MySpaceResponse(
        List<SpaceResponse> spaces,
        Integer pageNumber,
        Integer pageSize,
        Integer totalPages,
        Long totalResult,
        boolean first,
        boolean last
) {
}
