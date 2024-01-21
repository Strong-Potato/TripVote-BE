package fc.be.app.domain.notification.application;

import fc.be.app.domain.notification.application.dto.response.NotificationDetailResponse;
import fc.be.app.domain.notification.application.dto.response.NotificationsResponse;
import fc.be.app.domain.notification.repository.NotificationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
public class NotificationQueryService {

    private final NotificationRepository notificationRepository;

    public NotificationQueryService(
            NotificationRepository notificationRepository
    ) {
        this.notificationRepository = notificationRepository;
    }

    public NotificationsResponse findAllByMemberId(Long memberId) {
        return new NotificationsResponse(
                notificationRepository.findAllByReceiverId(memberId)
                        .stream()
                        .map(NotificationDetailResponse::from)
                        .toList()
        );
    }
}
