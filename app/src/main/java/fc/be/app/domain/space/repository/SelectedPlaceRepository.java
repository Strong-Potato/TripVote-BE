package fc.be.app.domain.space.repository;

import fc.be.app.domain.space.entity.Journey;
import fc.be.app.domain.space.entity.SelectedPlace;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SelectedPlaceRepository extends JpaRepository<SelectedPlace, Long> {

    Integer deleteByJourney(Journey journey);

    @Modifying
    @Query("DELETE FROM SelectedPlace sp WHERE sp.journey.id IN :journeyIds")
    void deleteByJourneyIds(@Param("journeyIds") List<Long> journeyIds);
}
