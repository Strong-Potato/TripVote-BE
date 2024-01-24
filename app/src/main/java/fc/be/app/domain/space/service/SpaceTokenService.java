package fc.be.app.domain.space.service;

import fc.be.app.domain.space.dto.response.SpaceIdResponse;
import fc.be.app.domain.space.dto.response.SpaceResponse;
import fc.be.app.domain.space.entity.JoinedMember;
import fc.be.app.domain.space.entity.Space;
import fc.be.app.domain.space.exception.SpaceException;
import fc.be.app.domain.space.repository.JoinedMemberRepository;
import fc.be.app.domain.space.repository.SpaceRepository;
import fc.be.app.domain.space.vo.SpaceToken;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static fc.be.app.domain.space.exception.SpaceErrorCode.SPACE_NOT_FOUND;

@RequiredArgsConstructor
@Service
public class SpaceTokenService {
    private final RedisTemplate<String, SpaceToken> redisTemplateWithSpace;
    private final SpaceRepository spaceRepository;
    private final JoinedMemberRepository joinedMemberRepository;

    public void saveVisitedSpace(Long memberId, Long spaceId, LocalDate endDate) {
        Space space = spaceRepository.findById(spaceId)
                .orElseThrow(() -> new SpaceException(SPACE_NOT_FOUND));

        if (isSpaceReadOnly(space)) {
            return;
        }

        String key = "recent_space_member:" + memberId;
        SpaceToken spaceToken = SpaceToken.builder()
                .spaceId(spaceId)
                .memberId(memberId)
                .build();

        if (endDate == null) {
            redisTemplateWithSpace.opsForValue().set(key, spaceToken);
        } else {
            Duration timeout = Duration.between(LocalDateTime.now(), endDate.atTime(23, 59, 59));
            redisTemplateWithSpace.opsForValue().set(key, spaceToken, timeout.toMinutes());
        }
    }

    public SpaceResponse getRecentSpace(Long memberId) {
        String key = "recent_space_member:" + memberId;

        SpaceToken spaceToken = redisTemplateWithSpace.opsForValue().get(key);

        if (spaceToken == null || !isValidSpace(spaceToken.spaceId())) {
            JoinedMember joinedMember = findRecentJoinedMember(memberId, LocalDate.now());
            return SpaceResponse.of(joinedMember.getSpace());
        }

        Space space = spaceRepository.findById(spaceToken.spaceId())
                .orElseThrow(() -> new SpaceException(SPACE_NOT_FOUND));

        if (isSpaceReadOnly(space)) {
            JoinedMember joinedMember = findRecentJoinedMember(memberId, LocalDate.now());
            return SpaceResponse.of(joinedMember.getSpace());
        }

        return SpaceResponse.of(space);
    }

    private JoinedMember findRecentJoinedMember(Long memberId, LocalDate currentDate) {
        Page<JoinedMember> activeJoinedMemberBySpace = joinedMemberRepository.findActiveJoinedMemberBySpace(
                memberId,
                currentDate,
                PageRequest.of(0, 1)
        );

        if (activeJoinedMemberBySpace.isEmpty()) {
            throw new SpaceException(SPACE_NOT_FOUND);
        }

        return activeJoinedMemberBySpace.getContent().get(0);
    }

    private boolean isValidSpace(Long spaceId) {
        return spaceId != null && spaceRepository.existsById(spaceId);
    }

    private boolean isSpaceReadOnly(Space space) {
        return space.getEndDate() != null && space.isReadOnly(LocalDate.now());
    }
}
