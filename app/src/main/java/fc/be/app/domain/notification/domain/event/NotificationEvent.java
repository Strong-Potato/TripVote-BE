package fc.be.app.domain.notification.domain.event;

import fc.be.app.domain.notification.domain.event.vo.MemberEventInfo;
import fc.be.app.domain.notification.domain.event.vo.SpaceEventInfo;
import fc.be.app.domain.notification.domain.event.vo.VoteEventInfo;
import fc.be.app.domain.notification.entity.NotificationType;

import java.time.LocalDateTime;

public interface NotificationEvent {

    Long topicId();

    SpaceEventInfo spaceEventInfo();

    MemberEventInfo memberEventInfo();

    VoteEventInfo voteEventInfo();

    NotificationType eventType();

    LocalDateTime createdAt();
}
