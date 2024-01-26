package fc.be.app.domain.vote.repository;

import fc.be.app.domain.vote.entity.Vote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface VoteRepository extends JpaRepository<Vote, Long>, VoteRepositoryCustom {

    @Query("select v from Vote v where v.id not in (select vrm.voteId from VoteResultMember vrm where vrm.memberId = :memberId)")
    List<Vote> findVotesNotVotedByMember(Long memberId);
}
