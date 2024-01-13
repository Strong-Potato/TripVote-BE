package fc.be.app.domain.space.dto.response;

import fc.be.app.domain.space.entity.Journey;

import java.util.List;


public record JourneysResponse(
        List<JourneyResponse> journeyResponses
) {
    public static JourneysResponse from(List<Journey> journeys) {
        List<JourneyResponse> journeyResponses = journeys.stream()
                .map(JourneyResponse::from)
                .toList();
        return new JourneysResponse(journeyResponses);
    }
}
