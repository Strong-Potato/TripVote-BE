package fc.be.app.domain.vote.service.dto.request;

import java.util.List;

public record CandidateDeleteRequest(
        Long voteId,
        Long memberId,
        List<Long> candidateIds
) {
}
