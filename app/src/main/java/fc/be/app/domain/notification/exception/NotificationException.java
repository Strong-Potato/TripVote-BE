package fc.be.app.domain.notification.exception;

import fc.be.app.global.exception.BizException;

public class NotificationException extends BizException {

    public NotificationException(NotificationErrorCode errorCode) {
        super(errorCode.getTitle(), errorCode.getDetail(), errorCode.getResponseCode());
    }

}
