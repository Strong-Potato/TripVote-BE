package fc.be.notification.domain.message.body;

import fc.be.notification.domain.message.vo.SpaceInfo;

public record ReviewMessageBody(
        String receiverId,
        String notificationType,
        String createdAt,
        SpaceInfo spaceInfo
) {

}
