package fc.be.app.domain.vote.controller.dto.request;

import jakarta.validation.constraints.NotNull;

public record VotingApiRequest(
        @NotNull Long voteId,
        @NotNull Long candidateId
) {
}
