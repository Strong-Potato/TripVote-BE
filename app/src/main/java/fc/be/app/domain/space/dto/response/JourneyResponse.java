package fc.be.app.domain.space.dto.response;

import fc.be.app.domain.space.entity.Journey;
import fc.be.app.domain.space.entity.SelectedPlace;
import fc.be.openapi.tourapi.tools.category.CategoryFinder;
import lombok.Builder;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Builder
public record JourneyResponse(
        Long journeyId,
        LocalDate date,
        List<SelectedPlaceResponse> places
) {
    public static JourneyResponse from(Journey journey) {

        List<SelectedPlaceResponse> selectedPlaceResponses = new ArrayList<>();

        for (SelectedPlace place : journey.getPlace()) {
            Item item = Item.builder()
                    .placeId(place.getPlace().getId())
                    .contentTypeId(place.getPlace().getContentTypeId().getId())
                    .category(CategoryFinder.getCategoryByCode(place.getPlace().getCategory()))
                    .thumbnail(place.getPlace().getThumbnail())
                    .title(place.getPlace().getTitle())
                    .address(place.getPlace().getLocation().getAddress())
                    .addressDetail(place.getPlace().getLocation().getAddressDetail())
                    .latitude(place.getPlace().getLocation().getLatitude())
                    .longitude(place.getPlace().getLocation().getLongitude())
                    .build();

            selectedPlaceResponses.add(
                    SelectedPlaceResponse.builder()
                            .selectedId(place.getId())
                            .order(place.getOrders())
                            .place(item)
                            .build()
            );
        }


        return JourneyResponse.builder()
                .journeyId(journey.getId())
                .date(journey.getDate())
                .places(selectedPlaceResponses)
                .build();
    }


    @Builder
    private record SelectedPlaceResponse(
            Long selectedId,
            Integer order,
            Item place
    ) {
    }

    @Builder
    private record Item(
            Integer placeId, Integer contentTypeId,
            String title, String thumbnail,
            String address, String addressDetail,
            Double latitude, Double longitude,
            String category
    ) {
    }

}
