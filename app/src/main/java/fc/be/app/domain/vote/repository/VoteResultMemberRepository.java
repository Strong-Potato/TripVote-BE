package fc.be.app.domain.vote.repository;

import fc.be.app.domain.vote.entity.VoteResultMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface VoteResultMemberRepository extends JpaRepository<VoteResultMember, Long> {
    @Query("select vm.voteId from VoteResultMember vm where vm.memberId = :memberId and vm.spaceId = :spaceId")
    List<Long> findVoteIdsByMemberIdAndSpaceId(Long memberId, Long spaceId);

    boolean existsByMemberIdAndVoteId(Long memberId, Long voteId);

    @Query("select vm from VoteResultMember vm where vm.memberId = :memberId and vm.voteId = :voteId")
    Optional<VoteResultMember> findByMemberIdAndVoteId(Long memberId, Long voteId);

    @Query("delete from VoteResultMember vm where vm.memberId = :memberId and vm.voteId = :voteId")
    void deleteByMemberIdAndVoteId(Long memberId, Long voteId);

    @Modifying
    @Query(value = "INSERT INTO VoteResultMember (memberId, voteId, spaceId) SELECT :memberId, :voteId, :spaceId WHERE NOT EXISTS (SELECT 1 FROM VoteResultMember WHERE memberId = :memberId AND voteId = :voteId)",
            nativeQuery = true)
    void saveIfNotExists(@Param("memberId") Long memberId, @Param("voteId") Long voteId, @Param("spaceId") Long spaceId);
}
