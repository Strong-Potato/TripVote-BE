package fc.be.app.domain.notification.application;

import fc.be.app.domain.notification.entity.NotificationEntity;
import fc.be.app.domain.notification.exception.NotificationException;
import fc.be.app.domain.notification.repository.NotificationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static fc.be.app.domain.notification.exception.NotificationErrorCode.NOTIFICATION_NOT_FOUND;

@Transactional
@Service
public class NotificationCommandService {

    private final NotificationRepository notificationRepository;

    public NotificationCommandService(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    public void read(Long memberId, Long notificationId) {
        NotificationEntity entity = notificationRepository.findNotificationByIdAndReceiverId(notificationId, memberId)
                .orElseThrow(() -> new NotificationException(NOTIFICATION_NOT_FOUND));

        entity.read();
    }
}
