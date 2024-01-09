package fc.be.app.domain.space.repository;

import fc.be.app.domain.space.entity.Space;
import java.time.LocalDate;
import java.util.List;

public interface SpaceRepositoryCustom {

    List<Space> findByEndDateAndMember(LocalDate endDate, Long memberId, boolean isUpcoming);

}
