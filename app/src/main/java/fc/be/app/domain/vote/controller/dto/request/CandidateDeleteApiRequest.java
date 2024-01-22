package fc.be.app.domain.vote.controller.dto.request;

import jakarta.validation.constraints.NotNull;

import java.util.List;

public record CandidateDeleteApiRequest(
        @NotNull List<Long> candidateIds
) {

}
