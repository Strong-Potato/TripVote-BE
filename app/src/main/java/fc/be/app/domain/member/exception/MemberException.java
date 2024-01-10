package fc.be.app.domain.member.exception;

import fc.be.app.global.exception.BizException;

public class MemberException extends BizException {

    public MemberException(MemberErrorCode errorCode) {
        super(errorCode.getTitle(), errorCode.getDetail(), errorCode.getResponseCode());
    }
}
