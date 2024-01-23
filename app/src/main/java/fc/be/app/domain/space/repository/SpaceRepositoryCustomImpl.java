package fc.be.app.domain.space.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import fc.be.app.domain.space.entity.Space;
import fc.be.app.domain.space.vo.SpaceType;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

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
    public Page<Space> findByEndDateAndMember(LocalDate endDate, Long memberId, SpaceType type, Pageable pageRequest) {
        List<Space> content = queryFactory
                .selectFrom(space)
                .leftJoin(space.joinedMembers, joinedMember)
                .on(joinedMember.space.id.eq(space.id))
                .where(
                        eqLeftSpaceAndMember(false, memberId),
                        goeUpComingEndDate(endDate, type),
                        ltPastEndDate(endDate, type)
                )
                .offset(pageRequest.getOffset())
                .limit(pageRequest.getPageSize())
                .fetch();

        JPAQuery<Long> countQuery = queryFactory
                .select(space.count())
                .from(space)
                .leftJoin(space.joinedMembers, joinedMember)
                .on(joinedMember.space.id.eq(space.id))
                .where(
                        eqLeftSpaceAndMember(false, memberId),
                        goeUpComingEndDate(endDate, type),
                        ltPastEndDate(endDate, type)
                );

        return PageableExecutionUtils.getPage(content, pageRequest, countQuery::fetchOne);
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
        return space.endDate.goe(endDate).or(space.endDate.isNull());
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
