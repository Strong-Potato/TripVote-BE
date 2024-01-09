package fc.be.app.domain.space.repository;

import static fc.be.app.domain.space.entity.QJoinedMember.joinedMember;
import static fc.be.app.domain.space.entity.QSpace.space;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import fc.be.app.domain.space.entity.Space;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class SpaceRepositoryCustomImpl implements SpaceRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Space> findByEndDateAndMember(LocalDate endDate, Long memberId,
        boolean isUpcoming) {
        BooleanBuilder where = new BooleanBuilder();
        where.and(joinedMember.leftSpace.eq(false)).and(joinedMember.member.id.eq(memberId));

        if (isUpcoming) {
            where.and(space.endDate.goe(endDate));
        } else {
            where.and(space.endDate.lt(endDate));
        }

        return queryFactory
            .selectFrom(space)
            .leftJoin(space.joinedMembers, joinedMember)
            .on(joinedMember.space.id.eq(space.id))
            .where(where)
            .fetch();
    }
}
