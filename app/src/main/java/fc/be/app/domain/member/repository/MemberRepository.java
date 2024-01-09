package fc.be.app.domain.member.repository;

import fc.be.app.domain.member.entity.Member;
import fc.be.app.domain.member.vo.AuthProvider;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    List<Member> findByEmail(String email);

    List<Member> findByNickname(String nickname);

    Optional<Member> findByProviderAndEmail(AuthProvider provider, String email);

    boolean existsByProviderAndEmail(AuthProvider provider, String email);
}
