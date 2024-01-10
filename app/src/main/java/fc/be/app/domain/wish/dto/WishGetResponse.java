package fc.be.app.domain.wish.dto;

import fc.be.app.domain.place.Location;
import fc.be.app.domain.wish.entity.Wish;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

public record WishGetResponse(
        List<Item> wishes
) {
    public static WishGetResponse from(List<Wish> wishes) {
        return new WishGetResponse(wishes.stream()
                .map(wish -> new Item(
                        PlaceSearsh.builder()
                                .id(wish.getPlace().getId())
                                .contentTypeId(wish.getPlace().getContentTypeId().getId())
                                .title(wish.getPlace().getTitle())
                                .thumbnail(wish.getPlace().getThumbnail())
                                .location(wish.getPlace().getLocation())
                                .build(),
                        wish.getCreatedDate()
                )).toList()
        );
    }

    public record Item(
            PlaceSearsh place,
            LocalDateTime createdDate
    ) {
    }

    @Builder
    private record PlaceSearsh(
            Integer id,
            Integer contentTypeId,
            String title,
            String thumbnail,
            Location location
    ) {
    }
}
