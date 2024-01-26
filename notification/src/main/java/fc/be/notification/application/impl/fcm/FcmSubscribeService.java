package fc.be.notification.application.impl.fcm;

import com.google.firebase.messaging.FirebaseMessaging;
import fc.be.notification.application.NotificationSubscribePort;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Slf4j
@Service
public class FcmSubscribeService implements NotificationSubscribePort {

    private final FirebaseMessaging fcm;

    public FcmSubscribeService(
            FirebaseMessaging fcm
    ) {
        this.fcm = fcm;
    }

    @Override
    public void subscribeToTopics(List<Long> topicIds, String userToken) {
        try {
            for (Long topicId : topicIds) {
                fcm.subscribeToTopicAsync(Collections.singletonList(userToken), topicId.toString());
            }
            log.debug("Subscribed to topic: " + topicIds);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void unsubscribeToTopic(List<Long> topicIds, String userToken) {
        try {
            for (Long topicId : topicIds) {
                fcm.unsubscribeFromTopicAsync(Collections.singletonList(userToken), topicId.toString());
            }
            log.debug("Unsubscribed to topic: " + topicIds);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
