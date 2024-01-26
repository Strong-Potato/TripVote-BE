package fc.be.app.domain.notification.domain.event.vote;

import fc.be.app.domain.notification.domain.event.NotificationEvent;
import fc.be.app.domain.notification.domain.event.vo.MemberEventInfo;
import fc.be.app.domain.notification.domain.event.vo.SpaceEventInfo;
import fc.be.app.domain.notification.domain.event.vo.VoteEventInfo;
import fc.be.app.domain.notification.entity.NotificationType;

import java.time.LocalDateTime;

public record VoteEvent(
        Long topicId,
        MemberEventInfo memberEventInfo,
        SpaceEventInfo spaceEventInfo,
        VoteEventInfo voteEventInfo,
        NotificationType eventType,
        LocalDateTime createdAt
) implements NotificationEvent {
}
