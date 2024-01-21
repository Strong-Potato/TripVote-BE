package fc.be.app.domain.vote.repository;

import fc.be.app.domain.vote.entity.VoteResultMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface VoteResultMemberRepository extends JpaRepository<VoteResultMember, Long> {
    @Query("select vm.voteId from VoteResultMember vm where vm.memberId = :memberId and vm.spaceId = :spaceId")
    List<Long> findVoteIdsByMemberIdAndSpaceId(Long memberId, Long spaceId);
}
