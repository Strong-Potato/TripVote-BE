package fc.be.app.domain.wish.dto;

import fc.be.app.domain.place.Location;
import fc.be.app.domain.wish.entity.Wish;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

public record WishGetResponse(
        List<Item> wishes,
        Integer pageNumber,
        Integer pageSize,
        Integer totalPages,
        Long totalResult,
        boolean first,
        boolean last
) {
    public static WishGetResponse from(List<Wish> wishes, Integer pageNumber, Integer pageSize, Integer totalPages, Long totalResult, boolean first, boolean last) {
        return new WishGetResponse(wishes.stream()
                .map(wish -> new Item(
                        PlaceSearch.builder()
                                .id(wish.getPlace().getId())
                                .contentTypeId(wish.getPlace().getContentTypeId().getId())
                                .title(wish.getPlace().getTitle())
                                .thumbnail(wish.getPlace().getThumbnail())
                                .category(wish.getPlace().getCategory())
                                .location(wish.getPlace().getLocation())
                                .build(),
                        wish.getCreatedDate()))
                .toList(),
                pageNumber,
                pageSize,
                totalPages,
                totalResult,
                first,
                last
        );
    }

    public record Item(
            PlaceSearch place,
            LocalDateTime createdDate
    ) {
    }

    @Builder
    public record PlaceSearch(
            Integer id,
            Integer contentTypeId,
            String title,
            String thumbnail,
            String category,
            Location location
    ) {
    }
}
