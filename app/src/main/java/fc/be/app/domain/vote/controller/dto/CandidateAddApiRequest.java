package fc.be.app.domain.vote.controller.dto;

import jakarta.validation.constraints.NotNull;

import java.util.List;

import static fc.be.app.domain.vote.service.VoteManageUseCase.CandidateAddRequest.CandidateInfo;

public record CandidateAddApiRequest(
        @NotNull List<CandidateInfo> candidateInfos
) {
}
