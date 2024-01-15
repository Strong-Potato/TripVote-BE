package fc.be.app.domain.vote.repository;

import fc.be.app.domain.vote.entity.Vote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface VoteRepository extends JpaRepository<Vote, Long> {

    @Query("select v from Vote v where v.space.id = :spaceId")
    List<Vote> findAllBySpaceId(Long spaceId);
}
