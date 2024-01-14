package fc.be.app.domain.member.repository;

import fc.be.app.domain.member.entity.Member;
import fc.be.app.domain.member.vo.AuthProvider;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    /**
     * <strong>email이 unique하지 않음을 주의!</strong>
     *
     * @param email 찾고자 하는 유저의 이메일
     * @return 해당 이메일을 가진 유저들
     */
    List<Member> findByEmail(String email);

    /**
     * <strong>nickname이 unique하지 않음을 주의!</strong>
     *
     * @param nickname 찾고자 하는 유저의 닉네임
     * @return 해당 닉네임을 가진 유저들
     */
    List<Member> findByNickname(String nickname);

    Optional<Member> findByProviderAndEmail(AuthProvider provider, String email);

    boolean existsByProviderAndEmail(AuthProvider provider, String email);
}
