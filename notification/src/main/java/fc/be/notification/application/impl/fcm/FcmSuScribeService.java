package fc.be.notification.application.impl.fcm;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import fc.be.notification.application.NotificationSubscribePort;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Slf4j
@Service
public class FcmSuScribeService implements NotificationSubscribePort {

    private final FirebaseMessaging fcm;

    public FcmSuScribeService(
            FirebaseMessaging fcm
    ) {
        this.fcm = fcm;
    }

    @Override
    public void subscribeToTopic(Long topicId, String userToken) {
        try {
            fcm.subscribeToTopic(Collections.singletonList(userToken), topicId.toString());

            log.debug("Subscribed to topic: " + topicId);
        } catch (FirebaseMessagingException e) {
            e.printStackTrace();
        }
    }
}
