package fc.be.app.domain.vote.repository;

import fc.be.app.domain.vote.entity.Vote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface VoteRepository extends JpaRepository<Vote, Long>, VoteRepositoryCustom {

    @Query(value = "select v from Vote v where v.space.id in (select jm.space.id from JoinedMember jm where jm.member.id = :memberId) and v.space.id not in (" +
            "select vrm.spaceId from VoteResultMember vrm where vrm.memberId = :memberId)", nativeQuery = true)
    List<Vote> findMemberVotes(Long memberId);
}
