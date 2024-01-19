package fc.be.app.domain.vote.controller.dto.request;

import jakarta.validation.constraints.NotNull;

import java.util.List;

import static fc.be.app.domain.vote.service.dto.request.CandidateAddRequest.CandidateAddInfo;

public record CandidateAddApiRequest(
        @NotNull List<CandidateAddInfo> candidateInfos
) {
}
