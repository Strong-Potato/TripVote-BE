package fc.be.notification.application;

public interface NotificationSubscribePort {

    void subscribeToTopic(Long topicId, String userToken);
}
