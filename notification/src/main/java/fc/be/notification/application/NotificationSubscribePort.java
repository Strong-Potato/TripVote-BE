package fc.be.notification.application;

import java.util.List;

public interface NotificationSubscribePort {

    void subscribeToTopics(List<String> topicIds, String userToken);
    void unsubscribeToTopic(List<String> topicIds, String userToken);
}
