package fc.be.app.domain.space.repository;

import fc.be.app.domain.member.entity.Member;
import fc.be.app.domain.space.entity.JoinedMember;
import fc.be.app.domain.space.entity.Space;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface JoinedMemberRepository extends JpaRepository<JoinedMember, Long> {

    Optional<JoinedMember> findBySpaceAndMember(Space space, Member member);

    @Query("SELECT jm FROM JoinedMember jm " +
            "LEFT JOIN Space sp ON jm.space.id = sp.id " +
            "WHERE jm.leftSpace = false AND jm.member.id = :memberId AND sp.endDate >= :currentDate " +
            "ORDER BY jm.createdDate DESC")
    Page<JoinedMember> findActiveJoinedMemberBySpace(
            @Param("memberId") Long memberId,
            @Param("currentDate") LocalDate currentDate,
            Pageable pageable
    );

    @Query("select jm.member.id FROM JoinedMember jm WHERE jm.space.id = :spaceId")
    List<Long> findMemberIdsBySpaceId(Long spaceId);
}
