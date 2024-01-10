package fc.be.app.domain.space.repository;

import fc.be.app.domain.space.entity.Space;
import fc.be.app.domain.space.vo.SpaceType;
import java.time.LocalDate;
import java.util.List;

public interface SpaceRepositoryCustom {

    List<Space> findByEndDateAndMember(LocalDate endDate, Long memberId, SpaceType type);

}
