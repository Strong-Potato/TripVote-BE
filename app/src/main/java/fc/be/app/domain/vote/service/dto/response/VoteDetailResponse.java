package fc.be.app.domain.vote.service.dto.response;

import fc.be.app.domain.vote.service.dto.response.vo.CandidateInfo;
import fc.be.app.domain.vote.service.dto.response.vo.MemberProfile;

import java.util.List;

public record VoteDetailResponse(
        Long id,
        String title,
        String voteStatus,
        MemberProfile createdBy,
        List<CandidateInfo> candidates
) {
}
