package fc.be.app.domain.notification.application;

import fc.be.app.domain.notification.application.dto.request.TokenRegisterRequest;
import fc.be.app.domain.notification.entity.NotificationToken;
import fc.be.app.domain.notification.repository.NotificationTokenRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class NotificationTokenRegisterService {

    private final NotificationTokenRepository notificationTokenRepository;

    public NotificationTokenRegisterService(NotificationTokenRepository notificationTokenRepository) {
        this.notificationTokenRepository = notificationTokenRepository;
    }

    @Transactional
    public void registerFcmToken(final TokenRegisterRequest request) {
        final Long memberId = request.memberId();
        final String token = request.token();

        notificationTokenRepository.findByMemberId(memberId)
                .ifPresentOrElse(
                        oldToken -> oldToken.update(token),
                        () -> notificationTokenRepository.save(NotificationToken.of(token, memberId))
                );
    }
}
