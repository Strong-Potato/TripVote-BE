package fc.be.app.domain.vote.repository;

import fc.be.app.domain.vote.repository.dto.VotesQueryResponse;

import java.util.List;

public interface VoteRepositoryCustom {

    VotesQueryResponse findByVoteId(Long id);

    List<VotesQueryResponse> findBySpaceId(Long spaceId);
}
