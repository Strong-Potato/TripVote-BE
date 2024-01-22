package fc.be.app.domain.vote.service.service;

import fc.be.app.domain.member.entity.Member;
import fc.be.app.domain.member.exception.MemberException;
import fc.be.app.domain.member.repository.MemberRepository;
import fc.be.app.domain.space.entity.Space;
import fc.be.app.domain.space.exception.SpaceException;
import fc.be.app.domain.space.repository.SpaceRepository;
import fc.be.app.domain.vote.entity.Candidate;
import fc.be.app.domain.vote.entity.Vote;
import fc.be.app.domain.vote.entity.VoteResultMember;
import fc.be.app.domain.vote.exception.VoteException;
import fc.be.app.domain.vote.repository.VoteRepository;
import fc.be.app.domain.vote.repository.VoteResultMemberRepository;
import fc.be.app.domain.vote.service.dto.response.VoteDetailResponse;
import fc.be.app.domain.vote.service.dto.response.VoteResultResponse;
import fc.be.app.domain.vote.service.dto.response.VotesResponse;
import fc.be.app.domain.vote.service.dto.response.vo.CandidateInfo;
import fc.be.app.domain.vote.service.dto.response.vo.MemberProfile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;

import static fc.be.app.domain.member.exception.MemberErrorCode.MEMBER_NOT_FOUND;
import static fc.be.app.domain.space.exception.SpaceErrorCode.NOT_JOINED_MEMBER;
import static fc.be.app.domain.space.exception.SpaceErrorCode.SPACE_NOT_FOUND;
import static fc.be.app.domain.vote.exception.VoteErrorCode.VOTE_NOT_FOUND;
import static fc.be.app.domain.vote.repository.VoteRepositoryCustom.SearchCondition;
import static fc.be.app.domain.vote.service.dto.response.VoteResultResponse.CandidateResultResponse;
import static fc.be.app.domain.vote.service.dto.response.VotesResponse.ViewResultVoteIds;
import static fc.be.app.domain.vote.service.dto.response.VotesResponse.VotesResponseElement;

@Transactional(readOnly = true)
@Service
public class VoteInfoQueryService {

    private final VoteRepository voteRepository;
    private final SpaceRepository spaceRepository;
    private final MemberRepository memberRepository;
    private final VoteResultMemberRepository voteResultMemberRepository;

    public VoteInfoQueryService(VoteRepository voteRepository,
                                SpaceRepository spaceRepository,
                                MemberRepository memberRepository,
                                VoteResultMemberRepository voteResultMemberRepository
    ) {
        this.voteRepository = voteRepository;
        this.spaceRepository = spaceRepository;
        this.memberRepository = memberRepository;
        this.voteResultMemberRepository = voteResultMemberRepository;
    }

    public VotesResponse searchVotes(Long memberId, SearchCondition searchCondition) {
        Space space = spaceRepository.findById(searchCondition.getSpaceId())
                .orElseThrow(() -> new SpaceException(SPACE_NOT_FOUND));

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberException(MEMBER_NOT_FOUND));

        if (!space.isBelong(member)) {
            throw new SpaceException(NOT_JOINED_MEMBER);
        }

        final List<Vote> votesInSpace = voteRepository.search(searchCondition);

        List<Long> resultIds = voteResultMemberRepository.findVoteIdsByMemberIdAndSpaceId(memberId, searchCondition.getSpaceId());

        return new VotesResponse(votesInSpace
                .stream()
                .map(vote -> new VotesResponseElement(
                        vote.getId(),
                        vote.getTitle(),
                        vote.getStatus(),
                        MemberProfile.of(vote.getOwner()),
                        vote.getVotedMembers()
                                .stream()
                                .map(votedMember -> MemberProfile.of(votedMember.getMember()))
                                .toList()))
                .toList(),
                new ViewResultVoteIds(resultIds));
    }

    public VotesResponse findMemberVotes(Long memberId) {
        List<Vote> votesNotMemberVoted = voteRepository.findMemberVotes(memberId);

        return new VotesResponse(votesNotMemberVoted
                .stream()
                .map(vote -> new VotesResponseElement(
                        vote.getId(),
                        vote.getTitle(),
                        vote.getStatus(),
                        MemberProfile.of(vote.getOwner()),
                        vote.getVotedMembers()
                                .stream()
                                .map(votedMember -> MemberProfile.of(votedMember.getMember()))
                                .toList()))
                .toList(),
                ViewResultVoteIds.emptyIds()
        );
    }

    public VoteDetailResponse findByVoteId(Long voteId, Long memberId) {
        Vote vote = getByVoteId(voteId);

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberException(MEMBER_NOT_FOUND));

        if (!vote.getSpace().isBelong(member)) {
            throw new SpaceException(NOT_JOINED_MEMBER);
        }

        return new VoteDetailResponse(
                vote.getId(),
                vote.getTitle(),
                vote.getStatus(),
                MemberProfile.of(vote.getOwner()),
                vote.getCandidates()
                        .stream()
                        .map(candidate -> CandidateInfo.of(memberId, candidate))
                        .toList()
        );
    }

    private Vote getByVoteId(Long voteId) {
        return voteRepository.findById(voteId)
                .orElseThrow(() -> new VoteException(VOTE_NOT_FOUND));
    }

    public VoteResultResponse findResultByVoteId(Long voteId, Long memberId) {
        Vote vote = getByVoteId(voteId);

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberException(MEMBER_NOT_FOUND));

        if (!vote.getSpace().isBelong(member)) {
            throw new SpaceException(NOT_JOINED_MEMBER);
        }

        voteResultMemberRepository.save(VoteResultMember.of(memberId, voteId, vote.getSpace().getId()));

        return new VoteResultResponse(
                vote.getId(),
                vote.getTitle(),
                vote.getStatus(),
                MemberProfile.of(vote.getOwner()),
                vote.getCandidates()
                        .stream()
                        .sorted(sortByVotedCount())
                        .map(candidate -> CandidateResultResponse.of(memberId, candidate))
                        .toList()
        );
    }

    private static Comparator<Candidate> sortByVotedCount() {
        return Comparator.comparingInt(Candidate::getVotedCount).reversed();
    }
}
