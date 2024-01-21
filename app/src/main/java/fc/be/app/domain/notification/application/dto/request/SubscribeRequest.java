package fc.be.app.domain.notification.application.dto.request;

public record SubscribeRequest(
        boolean isGlobal,
        Long topicId,
        Long memberId
) {
}
