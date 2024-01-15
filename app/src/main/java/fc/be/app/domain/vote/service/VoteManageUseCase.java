package fc.be.app.domain.vote.service;

import java.util.List;

import static fc.be.app.domain.vote.service.VoteInfoQueryUseCase.*;

public interface VoteManageUseCase {

    Long createVote(VoteCreateRequest request);


    VoteResponse addCandidate(CandidateAddRequest request);


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
