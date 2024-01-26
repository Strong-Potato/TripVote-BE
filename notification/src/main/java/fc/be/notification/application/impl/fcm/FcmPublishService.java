package fc.be.notification.application.impl.fcm;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import fc.be.notification.application.NotificationPublishPort;
import org.springframework.stereotype.Service;

@Service
public class FcmPublishService implements NotificationPublishPort {

    private final FirebaseMessaging fcm;

    public FcmPublishService(FirebaseMessaging fcm) {
        this.fcm = fcm;
    }

    public void publishNotificationToTopic(NotificationPublishRequest request) {
        Message message = Message.builder()
                .setNotification(Notification
                        .builder()
                        .setBody(request.body())
                        .build())
                .setTopic(request.topicId().toString())
                .build();

        try {
            fcm.send(message);
        } catch (FirebaseMessagingException e) {
            throw new RuntimeException(e);
        }
    }
}
