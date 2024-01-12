package fc.be.app.domain.space.dto.response;

import fc.be.app.domain.space.entity.Journey;
import fc.be.app.domain.space.entity.SelectedPlace;
import fc.be.app.domain.space.entity.Space;
import java.time.LocalDate;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class JourneyResponse {

    private Long id;
    private Space space;
    private LocalDate date;
    private List<SelectedPlace> place;

    @Builder
    private JourneyResponse(Long id, Space space, LocalDate date, List<SelectedPlace> place) {
        this.id = id;
        this.space = space;
        this.date = date;
        this.place = place;
    }

    public static JourneyResponse from(Journey journey) {
        return JourneyResponse.builder()
            .id(journey.getId())
            .space(journey.getSpace())
            .date(journey.getDate())
            .place(journey.getPlace())
            .build();
    }

}
