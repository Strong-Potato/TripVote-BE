package fc.be.app.domain.space.repository;

import fc.be.app.domain.space.entity.Space;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpaceRepository extends JpaRepository<Space, Long>, SpaceRepositoryCustom {

}
