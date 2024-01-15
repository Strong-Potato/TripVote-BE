package fc.be.app.domain.vote.repository.dto;

import fc.be.app.domain.space.vo.VoteStatus;
import lombok.Getter;

import java.util.List;

@Getter
public class VotesQueryResponse {
    private Long voteId;
    private String title;
    private VoteStatus voteStatus;
    private Long ownerId;
    private String ownerNickname;
    private String ownerImage;
    private List<String> votedMemberProfiles;
}
