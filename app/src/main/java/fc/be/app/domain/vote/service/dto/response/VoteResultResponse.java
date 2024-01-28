package fc.be.app.domain.vote.service.dto.response;

import fc.be.app.domain.member.entity.Member;
import fc.be.app.domain.vote.entity.Candidate;
import fc.be.app.domain.vote.entity.Vote;
import fc.be.app.domain.vote.service.dto.response.vo.MemberProfile;
import fc.be.app.domain.vote.service.dto.response.vo.PlaceInfo;

import java.util.Comparator;
import java.util.List;

public record VoteResultResponse(
        Long id,
        String title,
        String voteStatus,
        MemberProfile createdBy,
        List<CandidateResultResponse> candidatesResponses
) {

    public record CandidateResultResponse(
            Long id,
            PlaceInfo placeInfo,
            MemberProfile createdBy,
            List<MemberProfile> votedMemberProfiles,
            String tagline,
            boolean amIVote,
            int voteCount,
            int rank
    ) {
        public static CandidateResultResponse of(Long memberId, Candidate candidate, int rank) {
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
                    candidate.getVotedCount(),
                    rank);
        }
    }

    public static VoteResultResponse of(Vote vote, Member member) {
        List<Integer> sortedCount = vote.getCandidates().stream()
                .map(Candidate::getVotedCount)
                .distinct()
                .sorted(Comparator.reverseOrder())
                .toList();

        return new VoteResultResponse(
                vote.getId(),
                vote.getTitle(),
                vote.getStatus().getDescription(),
                MemberProfile.of(vote.getOwner()),
                vote.getCandidates()
                        .stream()
                        .sorted(sortByVotedCount())
                        .map(candidate -> CandidateResultResponse.of(member.getId(), candidate, sortedCount.indexOf(candidate.getVotedCount()) + 1))
                        .toList()
        );
    }

    private static Comparator<Candidate> sortByVotedCount() {
        return Comparator.comparingInt(Candidate::getVotedCount).reversed();
    }
}
