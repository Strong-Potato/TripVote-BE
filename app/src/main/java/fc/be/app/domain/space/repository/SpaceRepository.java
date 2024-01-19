package fc.be.app.domain.space.repository;

import fc.be.app.domain.member.entity.Member;
import fc.be.app.domain.space.entity.Space;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface SpaceRepository extends JpaRepository<Space, Long>, SpaceRepositoryCustom {

    @Query("SELECT count(sp) FROM Space sp left join sp.joinedMembers jm on sp.id = jm.space.id" +
            " where jm.leftSpace = false and jm.member = :member")
    Integer countSpaceByJoinedMembers(Member member);

}
