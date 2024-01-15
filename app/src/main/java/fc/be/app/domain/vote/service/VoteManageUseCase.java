package fc.be.app.domain.vote.service;

import java.util.List;

public interface VoteManageUseCase {

    Long createVote(VoteCreateRequest request);


    VoteInfoQueryUseCase.VoteResponse addCandidate(CandidateAddRequest request);


    record VoteCreateRequest(
            Long memberId,
            Long spaceId,
            String title
    ) {

    }

    record CandidateAddRequest(
            Long memberId,
            Long voteId,
            List<CandidateInfo> candidateInfo
    ) {
        public record CandidateInfo(
                Integer placeId,
                String tagline
        ) {

        }
    }
}
