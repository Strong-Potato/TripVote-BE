package fc.be.app.domain.wish.repository;

import fc.be.app.domain.wish.entity.Wish;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface WishRepository extends JpaRepository<Wish, Long> {
    @Query("select w from Wish w join fetch w.place where w.memberId = :memberId")
    Page<Wish> findAllByMemberId(Long memberId, Pageable pageable);

    Long deleteByMemberIdAndPlaceId(Long memberId, Integer placeId);
}
