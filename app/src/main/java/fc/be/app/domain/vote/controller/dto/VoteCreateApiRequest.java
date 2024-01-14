package fc.be.app.domain.vote.controller.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record VoteCreateApiRequest(
        @NotNull Long spaceId,
        @Size(min = 1, max = 15) String title
) {
}
