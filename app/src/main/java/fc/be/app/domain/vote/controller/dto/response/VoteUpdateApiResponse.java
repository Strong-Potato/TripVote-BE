package fc.be.app.domain.vote.controller.dto.response;

import fc.be.app.domain.vote.entity.Vote;

public record VoteUpdateApiResponse(
        Long spaceId,
        String title
) {
    public static VoteUpdateApiResponse of(Vote vote) {
        return new VoteUpdateApiResponse(
                vote.getSpace().getId(),
                vote.getTitle()
        );
    }
}
