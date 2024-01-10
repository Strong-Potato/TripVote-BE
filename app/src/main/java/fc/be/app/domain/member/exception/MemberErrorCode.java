package fc.be.app.domain.member.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MemberErrorCode {
    MEMBER_NOT_FOUND(404, "MEMBER_NOT_FOUND", "해당 사용자가 존재하지 않습니다.")
    ;

    private final Integer responseCode;
    private final String title;
    private final String detail;
}
