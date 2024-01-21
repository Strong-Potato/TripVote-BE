package fc.be.app.domain.notification.application;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import fc.be.app.domain.notification.domain.event.NotificationEvent;
import fc.be.app.domain.notification.entity.NotificationEntity;
import fc.be.app.domain.notification.repository.NotificationRepository;
import fc.be.app.domain.space.repository.JoinedMemberRepository;
import fc.be.notification.application.NotificationPublishPort;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.List;

import static fc.be.notification.application.NotificationPublishPort.NotificationPublishRequest;

@Slf4j
@Component
public class NotificationEventListener {

    private final JoinedMemberRepository joinedMemberRepository;
    private final NotificationPublishPort notificationPublishPort;
    private final NotificationRepository notificationRepository;
    private final ObjectMapper objectMapper;

    public NotificationEventListener(
            JoinedMemberRepository joinedMemberRepository,
            NotificationPublishPort notificationPublishPort,
            NotificationRepository notificationRepository,
            ObjectMapper objectMapper
    ) {
        this.joinedMemberRepository = joinedMemberRepository;
        this.notificationPublishPort = notificationPublishPort;
        this.notificationRepository = notificationRepository;
        this.objectMapper = objectMapper;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @TransactionalEventListener
    public void createNotification(final NotificationEvent notificationEvent) {
        try {
            final String jsonData = objectMapper.writeValueAsString(notificationEvent);

            List<Long> memberIds = joinedMemberRepository.findMemberIdsBySpaceId(notificationEvent.topicId());

            List<NotificationEntity> entities = memberIds.stream()
                    .map(id -> NotificationEntity.createNewNotification(
                            notificationEvent.eventType(),
                            id,
                            notificationEvent.createdAt(),
                            jsonData))
                    .toList();

            notificationRepository.saveAll(entities);

            notificationPublishPort.publishNotificationToTopic(
                    new NotificationPublishRequest(notificationEvent.spaceEventInfo().spaceId(), jsonData)
            );
        } catch (JsonProcessingException e) {
            log.error("json 에러");
        } catch (Exception e) {
            log.error("파이어베이스 관련 에러, 알림 재요청 필요, {}", e.getMessage(), e);
        }
    }
}
