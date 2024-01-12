package fc.be.app.domain.review.dto;

import jakarta.validation.constraints.*;

import java.time.LocalDate;
import java.util.List;

public record ReviewEditRequest(
        @NotNull Long reviewId,
        @Min(1) @Max(5) Integer rating,
        @NotBlank String content,
        List<String> images,
        @PastOrPresent LocalDate visitedAt
) {
}
