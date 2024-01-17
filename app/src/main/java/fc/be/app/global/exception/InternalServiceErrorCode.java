package fc.be.app.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum InternalServiceErrorCode {
    NO_SUCH_FILE("NO_SUCH_FILE", "해당 경로에 파일이 없습니다");
    private final String title;
    private final String detail;
}
