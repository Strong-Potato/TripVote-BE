package fc.be.notification.application;

public interface NotificationPublishPort {
    void publishNotificationToTopic(NotificationPublishRequest request);

    record NotificationPublishRequest(
            Long topicId,
            String body
    ) {

    }
}
