package fc.be.app.domain.vote.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum VoteErrorCode {
    VOTE_NOT_FOUND(404, "VOTE_NOT_FOUND", "투표 정보가 존재하지 않습니다."),
    ;

    private final Integer responseCode;
    private final String title;
    private final String detail;
}
