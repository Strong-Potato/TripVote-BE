package fc.be.app.domain.place.exception;

import fc.be.app.global.exception.BizException;

public class PlaceException extends BizException {
    public PlaceException(PlaceErrorCode errorCode) {
        super(errorCode.getTitle(), errorCode.getDetail(), errorCode.getResponseCode());
    }

}
