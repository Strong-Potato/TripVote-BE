package fc.be.app.domain.notification.application;

import fc.be.app.domain.member.entity.Member;
import fc.be.app.domain.member.exception.MemberException;
import fc.be.app.domain.member.repository.MemberRepository;
import fc.be.app.domain.notification.application.dto.request.SubscribeRequest;
import fc.be.app.domain.notification.entity.NotificationToken;
import fc.be.app.domain.notification.exception.NotificationException;
import fc.be.app.domain.notification.repository.NotificationTokenRepository;
import fc.be.app.domain.space.entity.Space;
import fc.be.app.domain.space.exception.SpaceException;
import fc.be.app.domain.space.repository.SpaceRepository;
import fc.be.notification.application.NotificationSubscribePort;
import org.springframework.stereotype.Service;

import static fc.be.app.domain.member.exception.MemberErrorCode.*;
import static fc.be.app.domain.notification.exception.NotificationErrorCode.NOT_FOUND_TOKEN;
import static fc.be.app.domain.space.exception.SpaceErrorCode.*;

@Service
public class NotificationSubscribeService {
    private static final long GLOBAL_TOPIC_ID = 0;

    private final NotificationSubscribePort notificationSubscribePort;
    private final NotificationTokenRepository notificationTokenRepository;
    private final SpaceRepository spaceRepository;
    private final MemberRepository memberRepository;

    public NotificationSubscribeService(
            NotificationSubscribePort notificationSubscribePort,
            NotificationTokenRepository notificationTokenRepository,
            SpaceRepository spaceRepository,
            MemberRepository memberRepository
    ) {
        this.notificationSubscribePort = notificationSubscribePort;
        this.notificationTokenRepository = notificationTokenRepository;
        this.spaceRepository = spaceRepository;
        this.memberRepository = memberRepository;
    }

    public void subscribe(SubscribeRequest request) {
        Member member = memberRepository.findById(request.memberId())
                .orElseThrow(() -> new MemberException(MEMBER_NOT_FOUND));

        NotificationToken token = notificationTokenRepository.findByMemberId(request.memberId())
                .orElseThrow(() -> new NotificationException(NOT_FOUND_TOKEN));

        if (request.isGlobal()) {
            notificationSubscribePort.subscribeToTopic(GLOBAL_TOPIC_ID, token.getToken());
            return;
        }

        Space space = spaceRepository.findById(request.topicId())
                .orElseThrow(() -> new SpaceException(SPACE_NOT_FOUND));

        if (!space.isBelong(member)) {
            throw new SpaceException(NOT_JOINED_MEMBER);
        }

        notificationSubscribePort.subscribeToTopic(request.topicId(), token.getToken());
    }
}
