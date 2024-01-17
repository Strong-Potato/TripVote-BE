package fc.be.app.domain.place.dto;

import fc.be.openapi.google.dto.review.APIRatingResponse;
import fc.be.openapi.tourapi.dto.response.bone.PlaceDTO;
import lombok.Builder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public record PlacePopularGetResponse(List<Item> places) {

    public static PlacePopularGetResponse from(List<? extends PlaceDTO> places) {
        List<Item> items = new ArrayList<>();
        for (var place : places) {
            items.add(Item.builder()
                    .id(place.getId())
                    .contentTypeId(place.getContentTypeId())
                    .title(place.getTitle())
                    .thumbnail(place.getThumbnail())
                    .areaCode(place.getLocation().getAreaCode())
                    .sigunguCode(place.getLocation().getSigunguCode())
                    .category(place.getCategory())
                    .build()
            );
        }
        return new PlacePopularGetResponse(items);
    }

    public PlacePopularGetResponse with(List<APIRatingResponse> apiRatingResponses) {
        for (int i = 0; i < places.size(); i++) {
            var rating = Optional.ofNullable(apiRatingResponses.get(i).rating()).orElse(0.0);
            var updated = places.get(i).toBuilder().rating(rating).build();
            places.set(i, updated);
        }

        return new PlacePopularGetResponse(places);
    }

    @Builder(toBuilder = true)
    private record Item(
            Integer id, Integer contentTypeId,
            String title, String thumbnail,
            Integer areaCode, Integer sigunguCode,
            String category, Double rating
    ) {
    }
}
