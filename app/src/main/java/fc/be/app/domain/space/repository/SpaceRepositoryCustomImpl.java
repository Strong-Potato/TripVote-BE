package fc.be.app.domain.space.repository;

import static fc.be.app.domain.space.entity.QJoinedMember.joinedMember;
import static fc.be.app.domain.space.entity.QSpace.space;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import fc.be.app.domain.space.entity.Space;
import fc.be.app.domain.space.vo.SpaceType;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class SpaceRepositoryCustomImpl implements SpaceRepositoryCustom {

    private final JPAQueryFactory queryFactory;

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
        return joinedMember.leftSpace.eq(leftSpace).and(joinedMember.member.id.eq(memberId));
    }

    private BooleanExpression goeUpComingEndDate(LocalDate endDate, SpaceType type) {
        if (!SpaceType.UPCOMING.equals(type)) {
            return null;
        }
        return space.endDate.goe(endDate);
    }

    private BooleanExpression ltPastEndDate(LocalDate endDate, SpaceType type) {
        if (!SpaceType.PAST.equals(type)) {
            return null;
        }
        return space.endDate.lt(endDate);
    }
}
