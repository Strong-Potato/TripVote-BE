package fc.be.app.domain.wish.dto;

import jakarta.validation.constraints.Positive;

public record WishAddRequest(
        @Positive Integer placeId,
        @Positive Integer contentTypeId
) {

}
