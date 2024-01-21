package fc.be.app.domain.vote.service.dto.request;

public record VotingRequest(
        Long voteId,
        Long memberId,
        Long candidateId
) {
}
