package fc.be.app.domain.space.dto.response;

import lombok.Builder;

@Builder
public record SpaceIdResponse(
        Long id
) {

    public static SpaceIdResponse of(Long id) {
        return SpaceIdResponse.builder()
                .id(id)
                .build();
    }

}
