package fc.be.app.domain.space.repository;

import fc.be.app.domain.space.entity.Space;
import fc.be.app.domain.space.vo.SpaceType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;

public interface SpaceRepositoryCustom {

    Page<Space> findByEndDateAndMember(LocalDate endDate, Long memberId, SpaceType type, Pageable pageRequest);

}
