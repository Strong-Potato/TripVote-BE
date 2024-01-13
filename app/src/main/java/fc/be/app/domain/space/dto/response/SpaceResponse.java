package fc.be.app.domain.space.dto.response;

import fc.be.app.domain.space.entity.Space;
import java.time.LocalDate;
import lombok.Builder;

@Builder
public record SpaceResponse(
    Long id,
    String title,
    LocalDate startDate,
    LocalDate endDate
) {
    public static SpaceResponse of(Space space) {
        return
            SpaceResponse.builder()
            .id(space.getId())
            .title(space.getTitle())
            .startDate(space.getStartDate())
            .endDate(space.getEndDate())
            .build();
    }
}
