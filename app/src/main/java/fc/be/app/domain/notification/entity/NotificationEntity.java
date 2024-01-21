package fc.be.app.domain.notification.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "notifications")
@Entity
public class NotificationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Comment("알림 타입")
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private NotificationType type;

    @Comment("알림을 받는 회원 ID")
    @Column(nullable = false)
    private Long receiverId;

    @Comment("알림 생성 일자")
    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Comment("알림 Body 전문")
    @Column(columnDefinition = "MEDIUMTEXT")
    private String jsonData;

    @Comment("알림 읽음 여부")
    private boolean isRead;

    @Builder
    private NotificationEntity(Long id, NotificationType type, Long receiverId, LocalDateTime createdAt, String jsonData, boolean isRead) {
        this.id = id;
        this.type = type;
        this.receiverId = receiverId;
        this.createdAt = createdAt;
        this.jsonData = jsonData;
        this.isRead = isRead;
    }

    public static NotificationEntity createNewNotification(NotificationType notificationType, Long receiverId, LocalDateTime createdAt, String jsonBody) {
        return NotificationEntity.builder()
                .type(notificationType)
                .receiverId(receiverId)
                .createdAt(createdAt)
                .jsonData(jsonBody)
                .isRead(false)
                .build();
    }

    public boolean isOwner(final Long memberId) {
        return receiverId.equals(memberId);
    }

    public void read() {
        isRead = true;
    }
}
