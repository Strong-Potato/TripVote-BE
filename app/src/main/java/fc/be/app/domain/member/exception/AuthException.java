package fc.be.app.domain.member.exception;

import fc.be.app.global.exception.BizException;

public class AuthException extends BizException {
    public AuthException(AuthErrorCode errorCode) {
        super(errorCode.getTitle(), errorCode.getDetail(), errorCode.getResponseCode());
    }
}
