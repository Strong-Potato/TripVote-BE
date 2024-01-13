package fc.be.app.domain.space.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import fc.be.app.domain.space.entity.Space;
import fc.be.app.domain.space.vo.SpaceType;
import jakarta.persistence.EntityManager;

import java.time.LocalDate;
import java.util.List;

import static fc.be.app.domain.space.entity.QJoinedMember.joinedMember;
import static fc.be.app.domain.space.entity.QSpace.space;

public class SpaceRepositoryCustomImpl implements SpaceRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public SpaceRepositoryCustomImpl(EntityManager entityManager) {
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    @Override
    public List<Space> findByEndDateAndMember(LocalDate endDate, Long memberId,
                                              SpaceType type) {
        return queryFactory
                .selectFrom(space)
                .leftJoin(space.joinedMembers, joinedMember)
                .on(joinedMember.space.id.eq(space.id))
                .where(
                        eqLeftSpaceAndMember(false, memberId),
                        goeUpComingEndDate(endDate, type),
                        ltPastEndDate(endDate, type)
                )
                .fetch();
    }

    private BooleanExpression eqLeftSpaceAndMember(Boolean leftSpace, Long memberId) {
        if (leftSpace != null && memberId != null) {
            return joinedMember.leftSpace.eq(leftSpace).and(joinedMember.member.id.eq(memberId));
        }
        return null;
    }

    private BooleanExpression goeUpComingEndDate(LocalDate endDate, SpaceType type) {
        if (endDate == null || type == null) {
            return null;
        }
        if (!SpaceType.UPCOMING.equals(type)) {
            return null;
        }
        return space.endDate.goe(endDate);
    }

    private BooleanExpression ltPastEndDate(LocalDate endDate, SpaceType type) {
        if (endDate == null || type == null) {
            return null;
        }
        if (!SpaceType.PAST.equals(type)) {
            return null;
        }
        return space.endDate.lt(endDate);
    }
}
