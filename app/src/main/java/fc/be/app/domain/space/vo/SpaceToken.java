package fc.be.app.domain.space.vo;

import lombok.Builder;

@Builder
public record SpaceToken(
        Long spaceId,
        Long memberId
) {

}
