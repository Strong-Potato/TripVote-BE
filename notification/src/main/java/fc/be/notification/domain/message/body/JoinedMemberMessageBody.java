package fc.be.notification.domain.message.body;

import fc.be.notification.domain.message.vo.MemberInfo;

public record JoinedMemberMessageBody(
        String receiverId,
        String notificationType,
        String createdAt,
        MemberInfo newMemberInfo
) {
}
