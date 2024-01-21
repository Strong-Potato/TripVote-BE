package fc.be.app.domain.notification.domain.event.space;

import fc.be.app.domain.notification.domain.event.NotificationEvent;
import fc.be.app.domain.notification.domain.event.vo.MemberEventInfo;
import fc.be.app.domain.notification.domain.event.vo.SpaceEventInfo;
import fc.be.app.domain.notification.domain.event.vo.VoteEventInfo;
import fc.be.app.domain.notification.entity.NotificationType;

import java.time.LocalDateTime;

public record SpaceEvent(
        Long topicId,
        MemberEventInfo memberEventInfo,
        SpaceEventInfo spaceEventInfo,
        NotificationType eventType,
        LocalDateTime createdAt
) implements NotificationEvent {

    @Override
    public VoteEventInfo voteEventInfo() {
        throw new UnsupportedOperationException();
    }
}
