package fc.be.app.domain.space.repository;

import fc.be.app.domain.member.entity.Member;
import fc.be.app.domain.space.entity.Space;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;

public interface SpaceRepository extends JpaRepository<Space, Long>, SpaceRepositoryCustom {

    @Query("SELECT count(sp) FROM Space sp left join sp.joinedMembers jm on sp.id = jm.space.id" +
            " where jm.leftSpace = false and jm.member = :member and (sp.endDate >= :currentDate or sp.endDate is null)")
    Integer countSpaceByJoinedMembers(Member member, LocalDate currentDate);

}
