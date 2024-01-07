package fc.be.domain.review.repository;

import fc.be.domain.review.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    List<Review> findByPlaceId(Long placeId);

    List<Review> findByMemberId(Long memberId);
}
