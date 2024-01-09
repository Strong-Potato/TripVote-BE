package fc.be.app.domain.space.repository;

import fc.be.app.domain.space.entity.JoinedMember;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JoinedMemberRepository extends JpaRepository<JoinedMember, Long> {

}