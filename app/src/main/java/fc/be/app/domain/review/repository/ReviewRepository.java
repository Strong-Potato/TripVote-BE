package fc.be.app.domain.review.repository;

import fc.be.app.domain.review.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    List<Review> findByPlaceId(Integer placeId);

    List<Review> findByMemberId(Long memberId);
}
