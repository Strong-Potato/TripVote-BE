package fc.be.app.domain.vote.service.service;

import fc.be.app.domain.member.entity.Member;
import fc.be.app.domain.member.exception.MemberException;
import fc.be.app.domain.member.repository.MemberRepository;
import fc.be.app.domain.notification.domain.event.vo.MemberEventInfo;
import fc.be.app.domain.notification.domain.event.vo.SpaceEventInfo;
import fc.be.app.domain.notification.domain.event.vo.VoteEventInfo;
import fc.be.app.domain.notification.domain.event.vote.VoteEvent;
import fc.be.app.domain.notification.entity.NotificationType;
import fc.be.app.domain.place.Place;
import fc.be.app.domain.place.repository.PlaceRepository;
import fc.be.app.domain.space.entity.Space;
import fc.be.app.domain.space.exception.SpaceException;
import fc.be.app.domain.space.repository.SpaceRepository;
import fc.be.app.domain.space.vo.VoteStatus;
import fc.be.app.domain.vote.entity.Candidate;
import fc.be.app.domain.vote.entity.Vote;
import fc.be.app.domain.vote.exception.VoteErrorCode;
import fc.be.app.domain.vote.exception.VoteException;
import fc.be.app.domain.vote.repository.VoteRepository;
import fc.be.app.domain.vote.service.dto.request.CandidateAddRequest;
import fc.be.app.domain.vote.service.dto.request.VoteCreateRequest;
import fc.be.app.domain.vote.service.dto.response.VoteDetailResponse;
import fc.be.app.domain.vote.service.dto.response.vo.CandidateInfo;
import fc.be.app.domain.vote.service.dto.response.vo.MemberProfile;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

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
    private final PlaceRepository placeRepository;
    private final ApplicationEventPublisher eventPublisher;

    public VoteManageService(VoteRepository voteRepository,
                             SpaceRepository spaceRepository,
                             MemberRepository memberRepository,
                             PlaceRepository placeRepository, ApplicationEventPublisher eventPublisher
    ) {
        this.voteRepository = voteRepository;
        this.spaceRepository = spaceRepository;
        this.memberRepository = memberRepository;
        this.placeRepository = placeRepository;
        this.eventPublisher = eventPublisher;
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

        // NOTE : EventPublisher을 통한 알림 기능 사용 예시
        eventPublisher.publishEvent(new VoteEvent(space.getId(),
                new MemberEventInfo(requestMember.getId(), requestMember.getNickname(), requestMember.getProfile()),
                new SpaceEventInfo(space.getId(), space.getTitle()),
                new VoteEventInfo(savedVote.getId(), savedVote.getTitle()),
                NotificationType.VOTE_CREATED,
                LocalDateTime.now())
        );

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
        validateVote(vote);

        List<Integer> placeIds = extractPlaceIdsFromRequest(request);

        List<Place> places = placeRepository.findAllById(placeIds);

        for (Place place : places) {
            Optional<String> matchedTagline = findMatchTagline(request, place);

            vote.addCandidate(Candidate.createNewVote(place, requestMember, vote, matchedTagline.orElse("")));
        }

        // TODO : Sending a new Candidate Add event to all members of the space
        return new VoteDetailResponse(
                vote.getId(),
                vote.getTitle(),
                vote.getStatus(),
                MemberProfile.of(vote.getOwner()),
                vote.getCandidates().stream()
                        .map(candidate -> CandidateInfo.of(request.memberId(), candidate))
                        .toList());
    }

    private static void validateVote(Vote vote) {
        if (vote.isMax(CANDIDATE_COUNT_THRESHOLD)) {
            throw new VoteException(CANDIDATE_IS_MAX);
        }
    }

    private List<Integer> extractPlaceIdsFromRequest(CandidateAddRequest request) {
        return request.candidateAddInfo()
                .stream()
                .map(CandidateAddInfo::placeId)
                .toList();
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
        validateVote(vote);

        if (vote.getStatus() == VoteStatus.VOTING) {
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
        validateVote(vote);

        voteRepository.delete(vote);
    }
}
