package fc.be.tripvote.domain.space.vo;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum VoteStatus {
    VOTING("투표중"),
    DONE("투표완료");

    private final String text;
}
