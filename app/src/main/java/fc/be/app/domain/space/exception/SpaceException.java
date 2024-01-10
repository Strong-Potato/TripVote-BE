package fc.be.app.domain.space.exception;

import fc.be.app.global.exception.BizException;

public class SpaceException extends BizException {
    public SpaceException(SpaceErrorCode errorCode) {
        super(errorCode.getTitle(), errorCode.getDetail(), errorCode.getResponseCode());
    }

}
