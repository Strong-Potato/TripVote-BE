package fc.be.app.domain.space.dto.response;

import fc.be.app.domain.space.entity.Journey;
import fc.be.app.domain.space.entity.SelectedPlace;
import lombok.Builder;

import java.time.LocalDate;
import java.util.List;

@Builder
public record JourneyResponse(
        Long id,
        LocalDate date,
        List<SelectedPlace> place
) {
    public static JourneyResponse from(Journey journey) {
        return JourneyResponse.builder()
                .id(journey.getId())
                .date(journey.getDate())
                .place(journey.getPlace())
                .build();
    }
}
