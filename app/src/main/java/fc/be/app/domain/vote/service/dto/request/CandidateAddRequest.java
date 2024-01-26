package fc.be.app.domain.vote.service.dto.request;

import java.util.List;

public record CandidateAddRequest(
        Long memberId,
        Long voteId,
        List<CandidateAddInfo> candidateAddInfo
) {
    public record CandidateAddInfo(
            Integer placeId,
            Integer placeTypeId,
            String tagline
    ) {

    }
}
