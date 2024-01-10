package fc.be.app.domain.wish.dto;

import fc.be.app.domain.wish.entity.Wish;

import java.time.LocalDateTime;

public record WishAddResponse(
        Long memberId,
        Integer placeId,
        LocalDateTime modifiedDate
) {
    public static WishAddResponse from(Wish wish) {
        return new WishAddResponse(
                wish.getMemberId(),
                wish.getPlace().getId(),
                wish.getModifiedDate()
        );
    }
}
