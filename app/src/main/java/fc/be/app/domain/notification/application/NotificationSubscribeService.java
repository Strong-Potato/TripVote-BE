package fc.be.app.domain.notification.application;

import fc.be.app.domain.member.entity.Member;
import fc.be.app.domain.member.exception.MemberErrorCode;
import fc.be.app.domain.member.exception.MemberException;
import fc.be.app.domain.member.repository.MemberRepository;
import fc.be.app.domain.notification.entity.NotificationToken;
import fc.be.app.domain.notification.exception.NotificationException;
import fc.be.app.domain.notification.repository.NotificationTokenRepository;
import fc.be.app.domain.space.repository.JoinedMemberRepository;
import fc.be.notification.application.NotificationSubscribePort;
import org.springframework.stereotype.Service;

import java.util.List;

import static fc.be.app.domain.notification.exception.NotificationErrorCode.NOT_FOUND_TOKEN;

@Service
public class NotificationSubscribeService {
    private static final long GLOBAL_TOPIC_ID = 0;

    private final NotificationSubscribePort notificationSubscribePort;
    private final NotificationTokenRepository notificationTokenRepository;
    private final MemberRepository memberRepository;
    private final JoinedMemberRepository joinedMemberRepository;

    public NotificationSubscribeService(
            NotificationSubscribePort notificationSubscribePort,
            NotificationTokenRepository notificationTokenRepository,
            MemberRepository memberRepository, JoinedMemberRepository joinedMemberRepository
    ) {
        this.notificationSubscribePort = notificationSubscribePort;
        this.notificationTokenRepository = notificationTokenRepository;
        this.memberRepository = memberRepository;
        this.joinedMemberRepository = joinedMemberRepository;
    }

    public void subscribe(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberException(MemberErrorCode.MEMBER_NOT_FOUND));
        member.setSubscription(true);

        NotificationToken token = notificationTokenRepository.findByMemberId(memberId)
                .orElseThrow(() -> new NotificationException(NOT_FOUND_TOKEN));

        List<String> joinedSpaceIds = joinedMemberRepository.findSpaceIdsByMemberId(memberId).stream()
                .map(String::valueOf)
                .toList();

        notificationSubscribePort.subscribeToTopics(joinedSpaceIds, token.getToken());
    }

    public void unsubscribe(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberException(MemberErrorCode.MEMBER_NOT_FOUND));
        member.setSubscription(false);

        NotificationToken token = notificationTokenRepository.findByMemberId(memberId)
                .orElseThrow(() -> new NotificationException(NOT_FOUND_TOKEN));

        List<String> joinedSpaceIds = joinedMemberRepository.findSpaceIdsByMemberId(memberId).stream()
                .map(String::valueOf)
                .toList();

        notificationSubscribePort.unsubscribeToTopic(joinedSpaceIds, token.getToken());
    }
}
