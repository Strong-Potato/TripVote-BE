package fc.be.app.domain.vote.repository;

import fc.be.app.domain.vote.entity.Candidate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CandidateRepository extends JpaRepository<Candidate, Long> {

    @Query("select c from Candidate c where c.vote.id in :voteIds")
    List<Candidate> findCandidatesByVoteIds(List<Long> voteIds);
}
