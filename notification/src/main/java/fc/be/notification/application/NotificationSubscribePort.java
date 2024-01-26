package fc.be.notification.application;

import java.util.List;

public interface NotificationSubscribePort {

    void subscribeToTopics(List<Long> topicIds, String userToken);
    void unsubscribeToTopic(List<Long> topicIds, String userToken);
}
