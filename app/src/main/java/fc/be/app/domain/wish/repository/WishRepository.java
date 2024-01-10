package fc.be.app.domain.wish.repository;

import fc.be.app.domain.wish.entity.Wish;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WishRepository extends JpaRepository<Wish, Long> {
    Page<Wish> findAllByMemberId(Long memberId, Pageable pageable);

    Long deleteByMemberIdAndPlaceId(Long memberId, Integer placeId);
}
