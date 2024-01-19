package fc.be.app.domain.vote.repository;

import fc.be.app.domain.vote.entity.Vote;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VoteRepository extends JpaRepository<Vote, Long>, VoteRepositoryCustom {
}
