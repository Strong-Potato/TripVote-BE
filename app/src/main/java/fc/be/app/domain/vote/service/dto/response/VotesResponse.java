package fc.be.app.domain.vote.service.dto.response;

import fc.be.app.domain.vote.service.dto.response.vo.MemberProfile;
import fc.be.app.domain.vote.service.dto.response.vo.SpaceInfo;

import java.util.Collections;
import java.util.List;

public record VotesResponse(
        List<VotesResponseElement> voteResponse,
        ViewResultVoteIds viewResultVoteIds
) {
    public record VotesResponseElement(
            Long voteId,
            String title,
            String voteStatus,
            MemberProfile createdBy,
            List<MemberProfile> votedMemberProfiles,
            SpaceInfo spaceInfo
    ) {

    }

    public record ViewResultVoteIds(
            List<Long> voteIds
    ) {
        public static ViewResultVoteIds emptyIds() {
            return new ViewResultVoteIds(Collections.emptyList());
        }
    }
}
