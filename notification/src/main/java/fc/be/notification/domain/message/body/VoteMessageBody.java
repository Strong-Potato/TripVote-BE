package fc.be.notification.domain.message.body;

public record VoteMessageBody(
        String receiverId,
        String notificationType,
        String createdAt,
        String voteTitle,
        String spaceId,
        String spaceName
) {

}
