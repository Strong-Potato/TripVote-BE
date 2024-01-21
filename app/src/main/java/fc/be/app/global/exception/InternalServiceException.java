package fc.be.app.global.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.ErrorResponseException;

import java.util.Map;

/**
 * <h3>
 * API 스펙을 충족했으나 서버 내부의 문제로 완료되지 않은 요청
 * </h3>
 * <p>
 * 서버에서는 HttpStatus: 5XX 응답을 해야 합니다.
 * RFC7807 규약을 따르기 위해 ErrorResponseException 을 상속받아서 {@link GlobalExceptionHandler} 에서 처리하도록 구현하였습니다.
 * </p>
 * <br>
 *
 * @author donghar
 * @see BizException
 */
public class InternalServiceException extends ErrorResponseException {
    public InternalServiceException(InternalServiceErrorCode errorCode) {
        this(errorCode, null);
    }

    public InternalServiceException(InternalServiceErrorCode errorCode, Throwable cause) {
        this(errorCode, null, cause);
    }

    public InternalServiceException(InternalServiceErrorCode errorCode, Map<String, Object> properties, Throwable cause) {
        super(HttpStatus.INTERNAL_SERVER_ERROR, cause);
        super.setTitle(errorCode.getTitle());
        super.setDetail(errorCode.getDetail());
        super.getBody().setProperties(properties);
    }
}
