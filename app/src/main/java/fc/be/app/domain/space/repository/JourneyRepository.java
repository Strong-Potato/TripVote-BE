package fc.be.app.domain.space.repository;

import fc.be.app.domain.space.entity.Journey;
import fc.be.app.domain.space.entity.Space;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface JourneyRepository extends JpaRepository<Journey, Long> {

    List<Journey> findAllBySpaceOrderByDateAsc(Space space);

    @Override
    void deleteAllByIdInBatch(Iterable<Long> longs);

    @Query("select r from Journey r " +
            "join fetch r.place sp " +
            "where r.id in (:journeyIds)")
    List<Journey> findAllByIdIn(List<Long> journeyIds);
}
