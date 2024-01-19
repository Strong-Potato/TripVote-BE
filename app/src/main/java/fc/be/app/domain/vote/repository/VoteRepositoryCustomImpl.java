package fc.be.app.domain.vote.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import fc.be.app.domain.space.vo.VoteStatus;
import fc.be.app.domain.vote.entity.Vote;
import fc.be.app.domain.vote.repository.dto.VotesQueryResponse;
import fc.be.app.domain.vote.service.dto.request.VoteStatusOption;
import jakarta.persistence.EntityManager;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

import static fc.be.app.domain.vote.entity.QVote.vote;


public class VoteRepositoryCustomImpl implements VoteRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    public VoteRepositoryCustomImpl(EntityManager entityManager) {
        this.jpaQueryFactory = new JPAQueryFactory(entityManager);
    }

    @Override
    public VotesQueryResponse findByVoteId(Long id) {
        jpaQueryFactory
                .select()
                .from(vote)
                .where(vote.id.eq(id));

        return null;
    }

    @Override
    public List<Vote> search(SearchCondition searchCondition) {
        return jpaQueryFactory
                .select(vote)
                .from(vote)
                .where(booleanBuilderProvider(searchCondition))
                .fetch();
    }

    private BooleanBuilder booleanBuilderProvider(SearchCondition searchCondition) {
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        if (searchCondition.getSpaceId() != null) {
            booleanBuilder.and(vote.id.eq(searchCondition.getSpaceId()));
        }

        if (searchCondition.getVoteStatusOption() != VoteStatusOption.ALL) {
            booleanBuilder.and(vote.status.eq(VoteStatus.valueOf(searchCondition.getVoteStatusOption().name())));
        }

        return booleanBuilder;
    }
}
