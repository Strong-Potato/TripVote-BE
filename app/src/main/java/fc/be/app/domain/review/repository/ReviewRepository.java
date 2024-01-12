package fc.be.app.domain.review.repository;

import fc.be.app.domain.review.entity.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    Page<Review> findByMemberId(Long memberId, Pageable pageable);

    Page<Review> findByPlaceId(Integer placeId, Pageable pageable);

    Integer deleteByIdAndMemberId(Long reviewId, Long memberId);

    @Query("SELECT count(r), avg(r.rating) FROM Review r WHERE r.place.id = :placeId")
    List<Number> countAndAverageRatingByPlaceId(@Param("placeId") Integer placeId);
}