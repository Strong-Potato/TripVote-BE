package fc.be.app.domain.notification.application;

import fc.be.app.domain.member.repository.MemberRepository;
import fc.be.app.domain.notification.entity.NotificationToken;
import fc.be.app.domain.notification.exception.NotificationException;
import fc.be.app.domain.notification.repository.NotificationTokenRepository;
import fc.be.app.domain.space.repository.JoinedMemberRepository;
import fc.be.app.domain.space.repository.SpaceRepository;
import fc.be.notification.application.NotificationSubscribePort;
import org.springframework.stereotype.Service;

import java.util.List;

import static fc.be.app.domain.notification.exception.NotificationErrorCode.NOT_FOUND_TOKEN;

@Service
public class NotificationSubscribeService {
    private static final long GLOBAL_TOPIC_ID = 0;

    private final NotificationSubscribePort notificationSubscribePort;
    private final NotificationTokenRepository notificationTokenRepository;
    private final SpaceRepository spaceRepository;
    private final MemberRepository memberRepository;
    private final JoinedMemberRepository joinedMemberRepository;

    public NotificationSubscribeService(
            NotificationSubscribePort notificationSubscribePort,
            NotificationTokenRepository notificationTokenRepository,
            SpaceRepository spaceRepository,
            MemberRepository memberRepository, JoinedMemberRepository joinedMemberRepository
    ) {
        this.notificationSubscribePort = notificationSubscribePort;
        this.notificationTokenRepository = notificationTokenRepository;
        this.spaceRepository = spaceRepository;
        this.memberRepository = memberRepository;
        this.joinedMemberRepository = joinedMemberRepository;
    }

    public void subscribe(Long memberId) {
        NotificationToken token = notificationTokenRepository.findByMemberId(memberId)
                .orElseThrow(() -> new NotificationException(NOT_FOUND_TOKEN));

        List<Long> joinedSpaceIds = joinedMemberRepository.findSpaceIdsByMemberId(memberId);
        joinedSpaceIds.add(GLOBAL_TOPIC_ID);

        notificationSubscribePort.subscribeToTopics(joinedSpaceIds, token.getToken());
    }

    public void unsubscribe(Long memberId) {
        NotificationToken token = notificationTokenRepository.findByMemberId(memberId)
                .orElseThrow(() -> new NotificationException(NOT_FOUND_TOKEN));

        var joinedSpaceIds = joinedMemberRepository.findSpaceIdsByMemberId(memberId);

        notificationSubscribePort.unsubscribeToTopic(joinedSpaceIds, token.getToken());
    }
}
