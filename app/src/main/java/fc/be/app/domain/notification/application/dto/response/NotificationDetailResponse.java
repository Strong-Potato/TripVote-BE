package fc.be.app.domain.notification.application.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import fc.be.app.domain.notification.entity.NotificationEntity;
import fc.be.app.domain.notification.entity.NotificationType;

import java.time.LocalDateTime;

public record NotificationDetailResponse(
        Long id,
        NotificationType type,
        String notificationInformation,
        @JsonProperty(value = "isRead")
        boolean isRead,
        Long receiverId,
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        LocalDateTime createdAt
) {
    public static NotificationDetailResponse from(final NotificationEntity entity) {
        return new NotificationDetailResponse(
                entity.getId(),
                entity.getType(),
                entity.getJsonData(),
                entity.isRead(),
                entity.getReceiverId(),
                entity.getCreatedAt()
        );
    }
}
