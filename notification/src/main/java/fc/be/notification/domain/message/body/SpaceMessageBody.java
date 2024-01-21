package fc.be.notification.domain.message.body;

import fc.be.notification.domain.message.vo.MemberInfo;
import fc.be.notification.domain.message.vo.SpaceInfo;

public record SpaceMessageBody(
        String receiverId,
        String notificationType,
        String createdAt,
        SpaceInfo spaceInfo,
        MemberInfo from
) {
}


