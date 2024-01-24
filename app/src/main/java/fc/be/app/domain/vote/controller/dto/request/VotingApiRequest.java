package fc.be.app.domain.vote.controller.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record VotingApiRequest(
        @Positive @NotNull Long voteId,
        @Positive @NotNull Long candidateId
) {
}
