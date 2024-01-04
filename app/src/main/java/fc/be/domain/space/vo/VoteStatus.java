package fc.be.domain.space.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum VoteStatus {
    VOTING("투표중"),
    DONE("투표완료");

    private final String description;
}
