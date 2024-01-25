package fc.be.app.domain.vote.service.dto.response.vo;

import fc.be.app.domain.space.entity.Space;

import java.time.LocalDate;

public record SpaceInfo(
        Long spaceId,
        String title,
        LocalDate startDate,
        LocalDate endDate
) {
    public static SpaceInfo of(Space space) {
        return new SpaceInfo(space.getId(), space.getCityToString(), space.getStartDate(), space.getEndDate());
    }
}
