package fc.be.app.domain.space.repository;

import fc.be.app.domain.member.entity.Member;
import fc.be.app.domain.space.entity.JoinedMember;
import fc.be.app.domain.space.entity.Space;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JoinedMemberRepository extends JpaRepository<JoinedMember, Long> {

    Optional<JoinedMember> findBySpaceAndMember(Space space, Member member);
}