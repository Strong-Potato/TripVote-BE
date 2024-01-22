package fc.be.app.domain.review.repository;

import fc.be.app.domain.review.dto.RatingStatisticsDTO;
import fc.be.app.domain.review.entity.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    @Query("SELECT r FROM Review r join fetch r.member join fetch r.place where r.member.id = :memberId")
    Page<Review> findByMemberId(Long memberId, Pageable pageable);

    Page<Review> findByPlaceId(Integer placeId, Pageable pageable);

    Integer deleteByIdAndMemberId(Long reviewId, Long memberId);

    Review findByIdAndMemberId(Long reviewId, Long memberId);

    @Query("SELECT new fc.be.app.domain.review.dto.RatingStatisticsDTO(avg(r.rating), count(r)) FROM Review r WHERE r.place.id = :placeId")
    RatingStatisticsDTO findRatingStatisticsByPlaceId(@Param("placeId") Integer placeId);
}