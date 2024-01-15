package fc.be.app.domain.vote.service;

public interface VoteManageUseCase {

    Long createVote(VoteCreateRequest request);

    record VoteCreateRequest(
            Long memberId,
            Long spaceId,
            String title
    ) {

    }
}
