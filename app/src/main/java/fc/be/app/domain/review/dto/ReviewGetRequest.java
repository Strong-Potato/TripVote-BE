package fc.be.app.domain.review.dto;

import jakarta.validation.constraints.NotNull;

public record ReviewGetRequest(
        @NotNull Integer placeId,
        @NotNull Integer contentTypeId,
        @NotNull String placeTitle
) {
}
