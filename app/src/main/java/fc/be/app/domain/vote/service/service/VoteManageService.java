package fc.be.app.domain.vote.service.service;

import fc.be.app.domain.member.entity.Member;
import fc.be.app.domain.member.exception.MemberException;
import fc.be.app.domain.member.repository.MemberRepository;
import fc.be.app.domain.space.entity.Space;
import fc.be.app.domain.space.exception.SpaceException;
import fc.be.app.domain.space.repository.SpaceRepository;
import fc.be.app.domain.vote.entity.Vote;
import fc.be.app.domain.vote.repository.VoteRepository;
import fc.be.app.domain.vote.service.VoteManageUseCase;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static fc.be.app.domain.member.exception.MemberErrorCode.MEMBER_NOT_FOUND;
import static fc.be.app.domain.space.exception.SpaceErrorCode.*;

@Service
public class VoteManageService implements VoteManageUseCase {

    private final VoteRepository voteRepository;
    private final SpaceRepository spaceRepository;
    private final MemberRepository memberRepository;

    public VoteManageService(VoteRepository voteRepository,
                             SpaceRepository spaceRepository,
                             MemberRepository memberRepository) {
        this.voteRepository = voteRepository;
        this.spaceRepository = spaceRepository;
        this.memberRepository = memberRepository;
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
}
