package fc.be.domain.review.exception;

import fc.be.global.exception.BizException;

public class ReviewException extends BizException {
    public ReviewException(ReviewErrorCode errorCode) {
        super(errorCode.getTitle(), errorCode.getDetail(), errorCode.getResponseCode());
    }
}
