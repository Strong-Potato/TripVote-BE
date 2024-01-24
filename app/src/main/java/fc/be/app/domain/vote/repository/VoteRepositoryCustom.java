package fc.be.app.domain.vote.repository;

import fc.be.app.domain.vote.entity.Vote;
import fc.be.app.domain.vote.service.dto.request.VoteStatusOption;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

public interface VoteRepositoryCustom {

    List<Vote> search(SearchCondition searchCondition);

    @NoArgsConstructor
    @Getter
    @Setter
    class SearchCondition {
        private Long spaceId;
        private VoteStatusOption voteStatusOption;

        public SearchCondition(Long spaceId, VoteStatusOption voteStatusOption) {
            this.spaceId = spaceId;

            if (voteStatusOption == null) {
                voteStatusOption = VoteStatusOption.ALL;
            }

            this.voteStatusOption = voteStatusOption;
        }
    }
}
