package fc.be.app.domain.vote.repository;

import fc.be.app.domain.vote.entity.VotedMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface VotedMemberRepository extends JpaRepository<VotedMember, Long> {

    @Query("select vm from VotedMember vm where vm.member.id = :memberId and vm.candidate.id = :candidateId")
    Optional<VotedMember> findByMemberIdAndCandidateId(Long memberId, Long candidateId);

    @Query("select vm from VotedMember vm join fetch vm.candidate where vm.vote.id=:voteId and vm.member.id=:memberId")
    List<VotedMember> findAllByMemberIdAndVoteId(Long memberId, Long voteId);

    @Modifying
    @Query("delete from VotedMember vm where vm.vote.id = :voteId")
    void deleteAllByVoteId(Long voteId);
}
