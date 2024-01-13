package fc.be.app.domain.space.dto.response;

import fc.be.app.domain.space.entity.Journey;
import fc.be.app.domain.space.entity.SelectedPlace;
import lombok.Builder;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Builder
public record JourneyResponse(
        Long id,
        LocalDate date,
        List<SelectedPlaceResponse> places
) {
    public static JourneyResponse from(Journey journey) {

        List<SelectedPlaceResponse> selectedPlaceResponses = new ArrayList<>();

        for (SelectedPlace place : journey.getPlace()) {
            Item item = Item.builder()
                    .id(place.getPlace().getId())
                    .category(place.getPlace().getCategory())
                    .thumbnail(place.getPlace().getThumbnail())
                    .title(place.getPlace().getTitle())
                    .address(place.getPlace().getLocation().getAddress())
                    .addressDetail(place.getPlace().getLocation().getAddressDetail())
                    .latitude(place.getPlace().getLocation().getLatitude())
                    .longitude(place.getPlace().getLocation().getLongitude())
                    .build();

            selectedPlaceResponses.add(
                    SelectedPlaceResponse.builder()
                            .id(place.getId())
                            .Order(place.getOrders())
                            .places(item)
                            .build()
            );
        }


        return JourneyResponse.builder()
                .id(journey.getId())
                .date(journey.getDate())
                .places(selectedPlaceResponses)
                .build();
    }


    @Builder
    private record SelectedPlaceResponse(
            Long id,
            Integer Order,
            Item places
    ) {
    }

    @Builder
    private record Item(
            Integer id, String title,
            String thumbnail, String address,
            String addressDetail, Double latitude,
            Double longitude, String category
    ) {
    }


}
