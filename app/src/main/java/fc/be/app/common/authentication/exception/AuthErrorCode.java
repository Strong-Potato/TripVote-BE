package fc.be.app.common.authentication.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AuthErrorCode {
    CODE_EXPIRED(201, "CODE_EXPIRED", "만료된 코드입니다. 다시 인증해주세요."),
    VERIFICATION_CODE_GENERATE_BLOCKED(202, "CODE_GENERATE_BLOCKED", "잠시 후 다시 요청해주세요."),
    INCORRECT_CODE(203, "INCORRECT_CODE", "잘못된 코드입니다."),
    TOKEN_EXPIRED(204, "TOKEN_EXPIRED", "만료된 토큰입니다. 다시 인증해주세요."),
    INCORRECT_TOKEN(205, "INCORRECT_TOKEN", "잘못된 토큰입니다."),
    INCORRECT_PASSWORD(206, "INCORRECT_PASSWORD", "잘못된 비밀번호 입니다."),
    SAME_PASSWORD(207, "SAME_PASSWORD", "새로운 비밀번호를 입력하세요."),
    LOGIN_REQUIRED(208, "LOGIN_REQUIRED", "로그인이 필요합니다.");

    private final int responseCode;
    private final String title;
    private final String detail;
}
