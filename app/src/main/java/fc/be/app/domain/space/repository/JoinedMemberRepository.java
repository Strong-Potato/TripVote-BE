package fc.be.app.domain.space.repository;

import fc.be.app.domain.member.entity.Member;
import fc.be.app.domain.space.entity.JoinedMember;
import fc.be.app.domain.space.entity.Space;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface JoinedMemberRepository extends JpaRepository<JoinedMember, Long> {

    Optional<JoinedMember> findBySpaceAndMember(Space space, Member member);
}