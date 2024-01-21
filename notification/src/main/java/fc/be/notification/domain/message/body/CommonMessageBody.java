package fc.be.notification.domain.message.body;

public record CommonMessageBody(
        String receiverId,
        String notificationType,
        String createdAt
) {

}
