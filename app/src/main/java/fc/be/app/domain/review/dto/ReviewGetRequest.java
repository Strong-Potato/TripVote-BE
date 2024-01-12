package fc.be.app.domain.review.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record ReviewGetRequest(
        @Positive Integer placeId,
        @NotNull Integer contentTypeId,
        @NotBlank String placeTitle
) {
}
