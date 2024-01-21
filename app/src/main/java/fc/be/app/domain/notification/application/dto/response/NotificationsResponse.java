package fc.be.app.domain.notification.application.dto.response;

import java.util.List;

public record NotificationsResponse(
        List<NotificationDetailResponse> notificationDetail
) {
}
