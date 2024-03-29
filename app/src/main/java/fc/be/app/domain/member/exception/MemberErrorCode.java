package fc.be.app.domain.member.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MemberErrorCode {
    EMAIL_ALREADY_EXISTS(101, "EMAIL_ALREADY_EXISTS", "중복된 이메일 입니다."),
    MEMBER_NOT_FOUND(404, "MEMBER_NOT_FOUND", "해당 사용자가 존재하지 않습니다.");

    private final int responseCode;
    private final String title;
    private final String detail;
}
