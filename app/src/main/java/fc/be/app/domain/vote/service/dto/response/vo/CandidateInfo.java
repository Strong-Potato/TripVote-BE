package fc.be.app.domain.vote.service.dto.response.vo;

import fc.be.app.domain.vote.entity.Candidate;

public record CandidateInfo(
        Long id,
        PlaceInfo placeInfo,
        MemberProfile createdBy,
        String tagline,
        boolean amIVote
) {

    public static CandidateInfo of(Long memberId, Candidate candidate) {
        return new CandidateInfo(
                candidate.getId(),
                PlaceInfo.of(candidate.getPlace()),
                MemberProfile.of(candidate.getOwner()),
                candidate.getTagline(),
                candidate.getVotedMember().stream()
                        .anyMatch(votedMember -> votedMember.isMemberVote(memberId)));
    }
}
