package fc.be.app.domain.vote.service.dto.request;

public record VoteCreateRequest(
        Long memberId,
        Long spaceId,
        String title
) {

}
