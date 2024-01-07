package fc.be.app.domain.place.repository;

import fc.be.app.domain.place.Place;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlaceRepository extends JpaRepository<Place, Integer> {
}
