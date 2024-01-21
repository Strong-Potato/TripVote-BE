package fc.be.app.domain.vote.service.dto.response;

import fc.be.app.domain.space.vo.VoteStatus;
import fc.be.app.domain.vote.entity.Candidate;
import fc.be.app.domain.vote.service.dto.response.vo.MemberProfile;
import fc.be.app.domain.vote.service.dto.response.vo.PlaceInfo;

import java.util.List;

public record VoteResultResponse(
        Long id,
        String title,
        VoteStatus voteStatus,
        MemberProfile ownerProfile,
        List<CandidateResultResponse> candidatesResponses
) {

    public record CandidateResultResponse(
            Long id,
            PlaceInfo placeInfo,
            MemberProfile ownerProfile,
            List<MemberProfile> votedMemberProfiles,
            String tagline,
            boolean amIVote,
            int voteCount
    ) {
        public static CandidateResultResponse of(Long memberId, Candidate candidate) {
            return new CandidateResultResponse(
                    candidate.getId(),
                    PlaceInfo.of(candidate.getPlace()),
                    MemberProfile.of(candidate.getOwner()),
                    candidate.getVotedMember().stream()
                            .map(votedMember -> MemberProfile.of(votedMember.getMember()))
                            .toList(),
                    candidate.getTagline(),
                    candidate.getVotedMember().stream()
                            .anyMatch(votedMember -> votedMember.isMemberVote(memberId)),
                    candidate.getVotedCount());
        }
    }
}
