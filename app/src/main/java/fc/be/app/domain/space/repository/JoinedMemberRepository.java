package fc.be.app.domain.space.repository;

import fc.be.app.domain.member.entity.Member;
import fc.be.app.domain.space.entity.JoinedMember;
import fc.be.app.domain.space.entity.Space;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.Optional;

public interface JoinedMemberRepository extends JpaRepository<JoinedMember, Long> {

    Optional<JoinedMember> findBySpaceAndMemberId(Space space, Long memberId);

    Optional<JoinedMember> findBySpaceAndMemberAndLeftSpace(Space space, Member member, Boolean isLeftSpace);

    @Query("SELECT jm FROM JoinedMember jm " +
            "LEFT JOIN Space sp ON jm.space.id = sp.id " +
            "WHERE jm.leftSpace = false AND jm.member.id = :memberId AND (sp.endDate >= :currentDate OR sp.endDate is null )" +
            "ORDER BY jm.createdDate DESC")
    Page<JoinedMember> findActiveJoinedMemberBySpace(
            @Param("memberId") Long memberId,
            @Param("currentDate") LocalDate currentDate,
            Pageable pageable
    );

    @Modifying
    @Query("UPDATE JoinedMember j SET j.leftSpace = :leftSpace WHERE j.member.id = :memberId")
    int updateAllByMemberId(@Param("memberId") Long memberId, @Param("leftSpace") boolean leftSpace);
}