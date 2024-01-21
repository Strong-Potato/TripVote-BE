package fc.be.app.domain.notification.repository;


import fc.be.app.domain.notification.entity.NotificationToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface NotificationTokenRepository extends JpaRepository<NotificationToken, Long> {

    Optional<NotificationToken> findByMemberId(final Long memberId);
}
