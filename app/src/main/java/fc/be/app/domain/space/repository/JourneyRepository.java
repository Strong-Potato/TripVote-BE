package fc.be.app.domain.space.repository;

import fc.be.app.domain.space.entity.Journey;
import fc.be.app.domain.space.entity.Space;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface JourneyRepository extends JpaRepository<Journey, Long> {

    List<Journey> findAllBySpaceOrderByDateAsc(Space space);

    @Override
    void deleteAllByIdInBatch(Iterable<Long> longs);
}
