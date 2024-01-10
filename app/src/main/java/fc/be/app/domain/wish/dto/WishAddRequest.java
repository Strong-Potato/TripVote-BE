package fc.be.app.domain.wish.dto;

import jakarta.validation.constraints.Positive;

public record WishAddRequest(
        @Positive Long memberId,
        @Positive Integer placeId,
        @Positive Integer contentTypeId
) {

}
