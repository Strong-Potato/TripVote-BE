package fc.be.app.domain.space.service;

import static fc.be.app.domain.member.exception.MemberErrorCode.MEMBER_NOT_FOUND;
import static fc.be.app.domain.space.exception.SpaceErrorCode.SPACE_NOT_FOUND;

import fc.be.app.domain.member.entity.Member;
import fc.be.app.domain.member.exception.MemberException;
import fc.be.app.domain.member.repository.MemberRepository;
import fc.be.app.domain.space.dto.request.UpdateSpaceRequest.DateUpdateRequest;
import fc.be.app.domain.space.dto.request.UpdateSpaceRequest.TitleUpdateRequest;
import fc.be.app.domain.space.dto.response.SpaceResponse;
import fc.be.app.domain.space.entity.JoinedMember;
import fc.be.app.domain.space.entity.Space;
import fc.be.app.domain.space.exception.SpaceException;
import fc.be.app.domain.space.repository.JoinedMemberRepository;
import fc.be.app.domain.space.repository.SpaceRepository;
import fc.be.app.domain.space.vo.SpaceType;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class SpaceService {

    private final SpaceRepository spaceRepository;
    private final JoinedMemberRepository joinedMemberRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public SpaceResponse createSpace(Long memberId) {
        Member member = memberRepository.findById(memberId)
            .orElseThrow(() -> new MemberException(MEMBER_NOT_FOUND));

        Space savedSpace = spaceRepository.save(Space.create());

        JoinedMember joinedMember = JoinedMember.create(savedSpace, member);
        joinedMemberRepository.save(joinedMember);

        return SpaceResponse.of(savedSpace);
    }

    public SpaceResponse getSpaceById(Long spaceId) {
        Space space = spaceRepository.findById(spaceId)
            .orElseThrow(() -> new SpaceException(SPACE_NOT_FOUND));
        return SpaceResponse.of(space);
    }

    @Transactional
    public SpaceResponse updateSpaceByTitle(Long spaceId, TitleUpdateRequest updateRequest) {
        Space space = spaceRepository.findById(spaceId)
            .orElseThrow(() -> new SpaceException(SPACE_NOT_FOUND));
        space.updateByTitle(updateRequest.getTitle());
        return SpaceResponse.of(space);
    }

    @Transactional
    public SpaceResponse updateSpaceByDates(Long spaceId, DateUpdateRequest updateRequest) {
        Space space = spaceRepository.findById(spaceId)
            .orElseThrow(() -> new SpaceException(SPACE_NOT_FOUND));
        space.updateByDates(updateRequest.getStartDate(), updateRequest.getEndDate());
        return SpaceResponse.of(space);
    }

    public List<SpaceResponse> findByEndDateAndMember(LocalDate currentDate, Long memberId, SpaceType type) {
        return spaceRepository.findByEndDateAndMember(currentDate, memberId, type)
            .stream()
            .map(SpaceResponse::of)
            .collect(Collectors.toList());
    }
}
