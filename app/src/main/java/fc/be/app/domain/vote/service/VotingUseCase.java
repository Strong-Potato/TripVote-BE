package fc.be.app.domain.vote.service;

/**
 * 투표 기능을 수행하는 API 입니다.
 */
public interface VotingUseCase {

    void vote(VoteRequest request);

    record VoteRequest(
            Long voteId,
            Long memberId,
            Long candidateId
    ) {
    }
}
