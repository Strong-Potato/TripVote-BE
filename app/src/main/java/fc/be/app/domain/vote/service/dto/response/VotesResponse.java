package fc.be.app.domain.vote.service.dto.response;

import fc.be.app.domain.space.vo.VoteStatus;
import fc.be.app.domain.vote.service.dto.response.vo.MemberProfile;

import java.util.List;

public record VotesResponse(
        List<VotesResponseElement> voteResponse
) {
    public record VotesResponseElement(
            Long voteId,
            String title,
            VoteStatus voteStatus,
            MemberProfile createdBy,
            List<MemberProfile> votedMemberProfiles
    ) {

    }
}
