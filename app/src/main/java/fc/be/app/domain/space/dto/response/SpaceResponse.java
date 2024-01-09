package fc.be.app.domain.space.dto.response;

import fc.be.app.domain.space.entity.Space;
import java.time.LocalDate;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SpaceResponse {

    private Long id;
    private String title;
    private LocalDate startDate;
    private LocalDate endDate;

    @Builder
    private SpaceResponse(Long id, String title, LocalDate startDate, LocalDate endDate) {
        this.id = id;
        this.title = title;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public static SpaceResponse of(Space space) {
        return SpaceResponse.builder()
            .id(space.getId())
            .title(space.getTitle())
            .startDate(space.getStartDate())
            .endDate(space.getEndDate())
            .build();
    }
}
