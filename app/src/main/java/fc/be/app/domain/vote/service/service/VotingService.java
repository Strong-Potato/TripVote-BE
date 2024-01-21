package fc.be.app.domain.vote.service.service;

import fc.be.app.domain.member.entity.Member;
import fc.be.app.domain.member.exception.MemberException;
import fc.be.app.domain.member.repository.MemberRepository;
import fc.be.app.domain.space.entity.Space;
import fc.be.app.domain.space.exception.SpaceException;
import fc.be.app.domain.vote.entity.Candidate;
import fc.be.app.domain.vote.entity.Vote;
import fc.be.app.domain.vote.exception.VoteException;
import fc.be.app.domain.vote.repository.CandidateRepository;
import fc.be.app.domain.vote.repository.VoteRepository;
import fc.be.app.domain.vote.repository.VotedMemberRepository;
import fc.be.app.domain.vote.service.dto.request.VotingRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static fc.be.app.domain.member.exception.MemberErrorCode.MEMBER_NOT_FOUND;
import static fc.be.app.domain.space.exception.SpaceErrorCode.NOT_JOINED_MEMBER;
import static fc.be.app.domain.space.exception.SpaceErrorCode.SPACE_IS_READ_ONLY;
import static fc.be.app.domain.vote.exception.VoteErrorCode.*;

@Service
@Transactional
public class VotingService {

    private final VoteRepository voteRepository;
    private final MemberRepository memberRepository;
    private final CandidateRepository candidateRepository;
    private final VotedMemberRepository votedMemberRepository;

    public VotingService(VoteRepository voteRepository,
                         MemberRepository memberRepository,
                         CandidateRepository candidateRepository,
                         VotedMemberRepository votedMemberRepository
    ) {
        this.voteRepository = voteRepository;
        this.memberRepository = memberRepository;
        this.candidateRepository = candidateRepository;
        this.votedMemberRepository = votedMemberRepository;
    }

    public void voteOrCancel(VotingRequest request) {
        Vote vote = voteRepository.findById(request.voteId())
                .orElseThrow(() -> new VoteException(VOTE_NOT_FOUND));

        Candidate candidate = candidateRepository.findById(request.candidateId())
                .orElseThrow(() -> new VoteException(CANDIDATE_NOT_FOUND));

        Member member = memberRepository.findById(request.memberId())
                .orElseThrow(() -> new MemberException(MEMBER_NOT_FOUND));

        Space space = vote.getSpace();

        if (!space.isBelong(member)) {
            throw new SpaceException(NOT_JOINED_MEMBER);
        }

        if (!space.isReadOnly(LocalDate.now())) {
            throw new SpaceException(SPACE_IS_READ_ONLY);
        }

        if (!vote.isStillVoting()) {
            throw new VoteException(VOTE_IS_DONE);
        }

        votedMemberRepository
                .findByMemberIdAndCandidateId(request.memberId(), request.candidateId())
                .ifPresentOrElse(entity -> {
                    votedMemberRepository.delete(entity);
                    candidate.decreaseVoteCount();
                }, () -> {
                    candidate.vote(vote, member);
                    candidate.increaseVoteCount();
                });
    }

}
