package fc.be.app.domain.review.exception;


import fc.be.app.global.exception.BizException;

public class ReviewException extends BizException {
    public ReviewException(ReviewErrorCode errorCode) {
        super(errorCode.getTitle(), errorCode.getDetail(), errorCode.getResponseCode());
    }
}
