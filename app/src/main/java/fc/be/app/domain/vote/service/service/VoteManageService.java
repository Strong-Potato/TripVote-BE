package fc.be.app.domain.vote.service.service;

import fc.be.app.domain.member.entity.Member;
import fc.be.app.domain.member.exception.MemberException;
import fc.be.app.domain.member.repository.MemberRepository;
import fc.be.app.domain.place.Place;
import fc.be.app.domain.place.repository.PlaceRepository;
import fc.be.app.domain.place.service.PlaceService;
import fc.be.app.domain.space.entity.Space;
import fc.be.app.domain.space.exception.SpaceException;
import fc.be.app.domain.space.repository.SpaceRepository;
import fc.be.app.domain.space.vo.VoteStatus;
import fc.be.app.domain.vote.controller.dto.request.VoteUpdateApiRequest;
import fc.be.app.domain.vote.controller.dto.response.VoteUpdateApiResponse;
import fc.be.app.domain.vote.entity.Candidate;
import fc.be.app.domain.vote.entity.Vote;
import fc.be.app.domain.vote.entity.VoteResultMember;
import fc.be.app.domain.vote.exception.VoteErrorCode;
import fc.be.app.domain.vote.exception.VoteException;
import fc.be.app.domain.vote.repository.CandidateRepository;
import fc.be.app.domain.vote.repository.VoteRepository;
import fc.be.app.domain.vote.repository.VoteResultMemberRepository;
import fc.be.app.domain.vote.repository.VotedMemberRepository;
import fc.be.app.domain.vote.service.dto.request.CandidateAddRequest;
import fc.be.app.domain.vote.service.dto.request.CandidateDeleteRequest;
import fc.be.app.domain.vote.service.dto.request.VoteCreateRequest;
import fc.be.app.domain.vote.service.dto.response.VoteDetailResponse;
import fc.be.app.domain.vote.service.dto.response.vo.CandidateInfo;
import fc.be.app.domain.vote.service.dto.response.vo.MemberProfile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static fc.be.app.domain.member.exception.MemberErrorCode.MEMBER_NOT_FOUND;
import static fc.be.app.domain.space.exception.SpaceErrorCode.*;
import static fc.be.app.domain.vote.exception.VoteErrorCode.CANDIDATE_IS_MAX;
import static fc.be.app.domain.vote.service.dto.request.CandidateAddRequest.CandidateAddInfo;

@Transactional
@Service
public class VoteManageService {

    private static final int CANDIDATE_COUNT_THRESHOLD = 15;

    private final VoteRepository voteRepository;
    private final SpaceRepository spaceRepository;
    private final MemberRepository memberRepository;
    private final PlaceService placeService;
    private final PlaceRepository placeRepository;
    private final CandidateRepository candidateRepository;
    private final VotedMemberRepository votedMemberRepository;
    private final VoteResultMemberRepository voteResultMemberRepository;


    public VoteManageService(VoteRepository voteRepository,
                             SpaceRepository spaceRepository,
                             MemberRepository memberRepository, PlaceService placeService,
                             PlaceRepository placeRepository,
                             CandidateRepository candidateRepository,
                             VotedMemberRepository votedMemberRepository,
                             VoteResultMemberRepository voteResultMemberRepository
    ) {
        this.voteRepository = voteRepository;
        this.spaceRepository = spaceRepository;
        this.memberRepository = memberRepository;
        this.placeService = placeService;
        this.placeRepository = placeRepository;
        this.candidateRepository = candidateRepository;
        this.votedMemberRepository = votedMemberRepository;
        this.voteResultMemberRepository = voteResultMemberRepository;
    }

    public Long createVote(VoteCreateRequest request) {

        final Member requestMember = memberRepository.findById(request.memberId())
                .orElseThrow(() -> new MemberException(MEMBER_NOT_FOUND));

        final Space space = spaceRepository.findById(request.spaceId())
                .orElseThrow(() -> new SpaceException(SPACE_NOT_FOUND));

        validateSpace(space, requestMember);

        Vote savedVote = voteRepository
                .save(Vote.of(space, request.title(), requestMember));

        // TODO : Sending a new vote creation event to all members of the space
        return savedVote.getId();
    }

    private static void validateSpace(Space space, Member requestMember) {
        if (space.isReadOnly(LocalDate.now())) {
            throw new SpaceException(SPACE_IS_READ_ONLY);
        }

        if (!space.isBelong(requestMember)) {
            throw new SpaceException(NOT_JOINED_MEMBER);
        }
    }

    public VoteDetailResponse addCandidate(CandidateAddRequest request) {
        final Member requestMember = memberRepository.findById(request.memberId())
                .orElseThrow(() -> new MemberException(MEMBER_NOT_FOUND));

        Vote vote = voteRepository.findById(request.voteId())
                .orElseThrow(() -> new VoteException(VoteErrorCode.VOTE_NOT_FOUND));

        final Space space = vote.getSpace();

        validateSpace(space, requestMember);

        if (vote.isMax(CANDIDATE_COUNT_THRESHOLD)) {
            throw new VoteException(CANDIDATE_IS_MAX);
        }

        Map<Integer, Integer> placesMap = extractPlaceMapFromRequest(request);

        List<Place> places = new ArrayList<>();

        for (var placeMap : placesMap.entrySet()) {
            places.add(placeService.saveOrUpdatePlace(placeMap.getKey(), placeMap.getValue()));
        }

        for (Place place : places) {
            Optional<String> matchedTagline = findMatchTagline(request, place);
            vote.addCandidate(Candidate.createNewVote(place, requestMember, vote, matchedTagline.orElse("")));
        }

        // TODO : Sending a new Candidate Add event to all members of the space
        return new VoteDetailResponse(
                vote.getId(),
                vote.getTitle(),
                vote.getStatus().getDescription(),
                MemberProfile.of(vote.getOwner()),
                vote.getCandidates().stream()
                        .map(candidate -> CandidateInfo.of(request.memberId(), candidate))
                        .toList());
    }

    private Map<Integer, Integer> extractPlaceMapFromRequest(CandidateAddRequest request) {
        return request.candidateAddInfo()
                .stream()
                .collect(Collectors.toMap(CandidateAddInfo::placeId, CandidateAddInfo::placeTypeId));
    }

    private Optional<String> findMatchTagline(CandidateAddRequest request, Place place) {
        return request.candidateAddInfo()
                .stream()
                .filter(info -> info.placeId().equals(place.getId()))
                .map(CandidateAddInfo::tagline)
                .findFirst();
    }

    public void changeVoteStatus(Long voteId, Long memberId) {
        final Member requestMember = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberException(MEMBER_NOT_FOUND));

        Vote vote = voteRepository.findById(voteId)
                .orElseThrow(() -> new VoteException(VoteErrorCode.VOTE_NOT_FOUND));

        final Space space = vote.getSpace();

        validateSpace(space, requestMember);

        if (vote.isStillVoting()) {
            vote.changeStatus(VoteStatus.DONE);
        } else {
            vote.changeStatus(VoteStatus.VOTING);
        }
    }

    public void deleteVote(Long voteId, Long memberId) {
        final Member requestMember = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberException(MEMBER_NOT_FOUND));

        Vote vote = voteRepository.findById(voteId)
                .orElseThrow(() -> new VoteException(VoteErrorCode.VOTE_NOT_FOUND));

        final Space space = vote.getSpace();

        validateSpace(space, requestMember);

        voteRepository.delete(vote);
    }

    public void deleteCandidates(CandidateDeleteRequest request) {
        final Member requestMember = memberRepository.findById(request.memberId())
                .orElseThrow(() -> new MemberException(MEMBER_NOT_FOUND));

        Vote vote = voteRepository.findById(request.voteId())
                .orElseThrow(() -> new VoteException(VoteErrorCode.VOTE_NOT_FOUND));

        final Space space = vote.getSpace();

        validateSpace(space, requestMember);

        candidateRepository.deleteAllById(request.candidateIds());
    }

    public void resetVote(Long voteId, Long memberId) {
        final Member requestMember = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberException(MEMBER_NOT_FOUND));

        Vote vote = voteRepository.findById(voteId)
                .orElseThrow(() -> new VoteException(VoteErrorCode.VOTE_NOT_FOUND));

        final Space space = vote.getSpace();

        validateSpace(space, requestMember);

        votedMemberRepository.deleteAllByVoteId(voteId);
        this.resetResultMode(voteId, memberId);
    }

    public void changeToResultMode(Long spaceId, Long voteId, Long memberId) {
        voteResultMemberRepository.findByMemberIdAndVoteId(memberId, voteId)
                .orElseGet(() -> voteResultMemberRepository.save(VoteResultMember.of(memberId, voteId, spaceId)));
    }

    public void resetResultMode(Long voteId, Long memberId) {
        voteResultMemberRepository
                .deleteByMemberIdAndVoteId(memberId, voteId);
    }

    public VoteUpdateApiResponse updateVoteTitle(Long voteId, Long memberId, VoteUpdateApiRequest request) {
        var vote = voteRepository.findById(voteId)
                .orElseThrow(() -> new VoteException(VoteErrorCode.VOTE_NOT_FOUND));

        final Member requestMember = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberException(MEMBER_NOT_FOUND));

        Space space = vote.getSpace();
        validateSpace(space, requestMember);
        vote.updateTitle(request.title());

        return VoteUpdateApiResponse.of(vote);
    }
}
