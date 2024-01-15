package fc.be.app.domain.vote.exception;

import fc.be.app.global.exception.BizException;

public class VoteException extends BizException {
    public VoteException(VoteErrorCode errorCode) {
        super(errorCode.getTitle(), errorCode.getDetail(), errorCode.getResponseCode());
    }

}
