package fc.be.app.domain.vote.service;

import fc.be.app.domain.space.vo.VoteStatus;
import fc.be.app.domain.vote.dto.MemberProfile;
import fc.be.app.domain.vote.entity.Candidate;

import java.util.List;

/**
 * 투표의 전체적인 정보를 쿼리할 수 있는 API 입니다.
 */
public interface VoteInfoQueryUseCase {

    VotesResponse findAllVotesInSpace(Long spaceId);

    VoteResponse findByVoteId(Long voteId);

    VoteResultResponse findResultByVoteId(Long voteId);

    record VoteResponse(
            Long id,
            String title,
            MemberProfile ownerProfile,
            List<CandidateResponse> candidates
    ) {
    }

    record VotesResponse(
            List<VotesResponseElement> data
    ) {

    }

    record VotesResponseElement(
            Long voteId,
            String title,
            VoteStatus voteStatus,
            MemberProfile ownerProfile,
            List<MemberProfile> votedMemberProfiles
    ) {

    }

    record CandidateResponse(
            Long id,
            Integer placeId,
            String placeName,
            String category,
            String tagline
    ) {

        public static CandidateResponse of(Candidate candidate) {
            return new CandidateResponse(
                    candidate.getId(),
                    candidate.getPlace().getId(),
                    candidate.getPlace().getThumbnail(),
                    candidate.getPlace().getCategory(),
                    candidate.getTagline());
        }
    }

    record VoteResultResponse() {

    }
}
