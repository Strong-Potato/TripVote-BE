package fc.be.app.domain.vote.service.service;

import fc.be.app.domain.member.entity.Member;
import fc.be.app.domain.member.exception.MemberException;
import fc.be.app.domain.member.repository.MemberRepository;
import fc.be.app.domain.place.Place;
import fc.be.app.domain.place.repository.PlaceRepository;
import fc.be.app.domain.space.entity.Space;
import fc.be.app.domain.space.exception.SpaceException;
import fc.be.app.domain.space.repository.SpaceRepository;
import fc.be.app.domain.vote.dto.MemberProfile;
import fc.be.app.domain.vote.entity.Candidate;
import fc.be.app.domain.vote.entity.Vote;
import fc.be.app.domain.vote.exception.VoteErrorCode;
import fc.be.app.domain.vote.exception.VoteException;
import fc.be.app.domain.vote.repository.VoteRepository;
import fc.be.app.domain.vote.service.VoteManageUseCase;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static fc.be.app.domain.member.exception.MemberErrorCode.MEMBER_NOT_FOUND;
import static fc.be.app.domain.space.exception.SpaceErrorCode.*;
import static fc.be.app.domain.vote.exception.VoteErrorCode.CANDIDATE_IS_MAX;
import static fc.be.app.domain.vote.service.VoteInfoQueryUseCase.CandidateResponse;
import static fc.be.app.domain.vote.service.VoteInfoQueryUseCase.VoteResponse;
import static fc.be.app.domain.vote.service.VoteManageUseCase.CandidateAddRequest.CandidateInfo;

@Service
public class VoteManageService implements VoteManageUseCase {

    private static final int CANDIDATE_COUNT_THRESHOLD = 15;

    private final VoteRepository voteRepository;
    private final SpaceRepository spaceRepository;
    private final MemberRepository memberRepository;
    private final PlaceRepository placeRepository;

    public VoteManageService(VoteRepository voteRepository,
                             SpaceRepository spaceRepository,
                             MemberRepository memberRepository,
                             PlaceRepository placeRepository) {
        this.voteRepository = voteRepository;
        this.spaceRepository = spaceRepository;
        this.memberRepository = memberRepository;
        this.placeRepository = placeRepository;
    }

    @Transactional
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

    @Override
    public VoteResponse addCandidate(CandidateAddRequest request) {
        final Member requestMember = memberRepository.findById(request.memberId())
                .orElseThrow(() -> new MemberException(MEMBER_NOT_FOUND));

        Vote vote = voteRepository.findById(request.voteId())
                .orElseThrow(() -> new VoteException(VoteErrorCode.VOTE_NOT_FOUND));

        final Space space = vote.getSpace();

        validateSpace(space, requestMember);
        validateVote(vote);

        List<Integer> placeIds = extractPlaceIdsFromRequest(request);

        List<Place> places = placeRepository.findAllById(placeIds);

        for (Place place : places) {
            Optional<String> first = findMatchTagline(request, place);

            vote.addCandidate(Candidate.of(place, vote, first.orElseGet(() -> "")));
        }

        // TODO : Sending a new vote creation event to all members of the space
        return new VoteResponse(
                vote.getId(),
                vote.getTitle(),
                new MemberProfile(
                        vote.getOwner().getId(),
                        vote.getOwner().getNickname(),
                        vote.getOwner().getProfile()
                ),
                vote.getCandidates().stream()
                        .map(CandidateResponse::of)
                        .toList());
    }

    private static void validateVote(Vote vote) {
        if (vote.isMax(CANDIDATE_COUNT_THRESHOLD)) {
            throw new VoteException(CANDIDATE_IS_MAX);
        }
    }

    private List<Integer> extractPlaceIdsFromRequest(CandidateAddRequest request) {
        return request.candidateInfo().stream()
                .map(CandidateInfo::placeId)
                .toList();
    }

    private Optional<String> findMatchTagline(CandidateAddRequest request, Place place) {
        return request.candidateInfo().stream()
                .filter(info -> info.placeId().equals(place.getId()))
                .map(CandidateInfo::tagline)
                .findFirst();
    }
}
