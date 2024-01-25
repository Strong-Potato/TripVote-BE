package fc.be.app.domain.space.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum VoteStatus {
    VOTING("진행 중"),
    DONE("결정완료");

    private final String description;
}
