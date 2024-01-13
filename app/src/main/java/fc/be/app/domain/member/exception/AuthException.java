package fc.be.app.domain.member.exception;

import fc.be.app.global.exception.BizException;

import java.util.Map;

public class AuthException extends BizException {
    public AuthException(AuthErrorCode errorCode) {
        super(errorCode.getTitle(), errorCode.getDetail(), errorCode.getResponseCode());
    }

    public AuthException(AuthErrorCode errorCode, String k1, Object v1) {
        super(errorCode.getTitle(), errorCode.getDetail(), Map.of("respnoseCode", errorCode.getResponseCode(), k1, v1));
    }
}
