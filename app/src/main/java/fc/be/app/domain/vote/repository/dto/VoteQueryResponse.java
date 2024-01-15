package fc.be.app.domain.vote.repository.dto;

import fc.be.app.domain.space.vo.VoteStatus;

public class VoteQueryResponse {
    private Long voteId;
    private String title;
    private String tagline;
    private VoteStatus voteStatus;
    private Long ownerId;
    private String ownerNickname;
    private String ownerImage;
}
