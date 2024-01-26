package fc.be.app.domain.notification.repository;

import fc.be.app.domain.notification.entity.NotificationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface NotificationRepository extends JpaRepository<NotificationEntity, Long> {

    @Query("select n from NotificationEntity n where n.receiverId = :receiverId order by n.createdAt desc ")
    List<NotificationEntity> findAllByReceiverId(Long receiverId);

    Optional<NotificationEntity> findNotificationByIdAndReceiverId(Long id, Long receiverId);

    List<NotificationEntity> findAllByIdIn(List<Long> notificationIds);

    @Modifying
    @Query("delete from NotificationEntity n where n.id in :ids")
    void deleteBatchByIdsIn(@Param("ids") List<Long> notificationIds);

    @Query("INSERT INTO VoteResultMember (memberId, voteId, spaceId) SELECT :memberId, :voteId, :spaceId WHERE NOT EXISTS (SELECT 1 FROM VoteResultMember WHERE memberId = :memberId AND voteId = :voteId)")
    void test();
}
