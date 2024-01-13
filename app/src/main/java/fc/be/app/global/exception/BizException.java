package fc.be.app.global.exception;

import jakarta.annotation.Nullable;
import org.springframework.http.HttpStatus;
import org.springframework.web.ErrorResponseException;

import java.util.Map;

/**
 * <p>
 * <img src="{@docRoot}/../resources/docs/response-flow.jpg">
 * </p>
 * <br>
 *
 * <h3>
 * 저희 서비스에서는 클라이언트의 4가지 종류의 요청에 대해 각기 다른 응답을 하게 됩니다.
 * </h3>
 * <ol>
 *     <li>API 스펙을 충족시키지 못한 요청</li>
 *     <li>API 스펙을 충족했으나 서비스상의 이유로 완료되지 않은 요청</li>
 *     <li>API 스펙을 충족했으나 DB나 외부 API 의 문제로 완료되지 않은 요청</li>
 *     <li>API 스펙을 충족했고 완료된 요청</li>
 * </ol>
 *
 * <h3>
 *     1. API 스펙을 충족시키지 못한 요청
 * </h3>
 * <p>
 *     API 스펙을 충족시키지 못한 요청은 `ArgumentResolver` 가 동작하는 시점에 ValidationException 을 던지게 되고, 이를 ({@link org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler} 를 상속하고 있는) {@link GlobalExceptionHandler} 가 받아 처리하게 됩니다.
 *     {@link org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler} 는 RFC7807 에 맞게 Exception 을 처리해서 응답하게 됩니다.
 * </p>
 * <br>
 *
 * <h3>
 *     2. API 스펙을 충족했으나 서비스상의 이유로 완료되지 않은 요청
 * </h3>
 * <p>
 *     API 스펙을 충족한 이상 서버에서는 HttpStatus: 2XX 응답을 해야 합니다.
 *     (BAD_REQUEST 로 응답하게 되면 클라이언트는 API 스펙을 충족한 요청을 보내고 있음에도 불구하고 문제를 클라이언트에서 찾게 될 것이기 때문입니다.)
 *     하지만 RFC7807 규약을 따르기 위해 BizException 이 ErrorResponseException 를 상속하게 되면 Spring({@link org.springframework.web.servlet.mvc.method.annotation.RequestResponseBodyMethodProcessor}) 은 HttpStatus 와 ProblemDetail 의 status 를 일치시게 구현되어 있어서 클라이언트와 통신할 코드(responseCode)가 추가로 필요하게 됩니다.
 * </p>
 * <br>
 *
 * <h3>
 *     3. API 스펙을 충족했으나 DB나 외부 API 의 문제로 완료되지 않은 요청
 * </h3>
 * <p>
 *     서버에서는 HttpStatus: 5XX 응답을 해야 합니다.
 *     하지만 이 경우까지 BizException 을 활용하게 되면 Service 단에서 BizException 을 발생시킬 때 HttpStatus 까지 고려해서 생성해야 하는 번거로움이 생기게 됩니다.
 *     따라서 별도의 Exception({@link ExternalServiceException}) 에서 처리하도록 구현하였습니다.
 * </p>
 * <br>
 *
 * <h3>
 *     4. API 스펙을 충족했고 완료된 요청
 * </h3>
 * <p>
 *     서버에서는 HttpStatus: 2XX 응답을 해야 합니다.
 *     또한 이때 Body 에는 ApiResponse 가 담기게 됩니다.
 * </p>
 * <br>
 *
 *
 * <h3>
 *     하단은 BizException 을 상속받아 구현된 Exception 및 ErrorCode 의 Sample Code 입니다.
 * </h3>
 *
 * <pre>
 *     {@code
 *      public class MemberException extends BizException {
 *          public MemberException(MemberErrorCode errorCode) {
 *              super(errorCode.getTitle(), errorCode.getDetail(), errorCode.getResponseCode());
 *          }
 *      }
 *
 *      public enum MemberErrorCode {
 *          EMAIL_DUPLICATE(101, "duplicate email", "중복된 이메일 입니다. 다른 이메일을 입력해주세요.");
 *
 *          private final Integer responseCode;
 *          private final String title;
 *          private final String detail;
 *
 *          MemberErrorCode(Integer responseCode, String title, String detail) {
 *              this.responseCode = responseCode;
 *              this.title = title;
 *              this.detail = detail;
 *          }
 *
 *          getter...
 *      }
 *     }
 * </pre>
 *
 * @author donghar
 * @see ExternalServiceException
 */
public abstract class BizException extends ErrorResponseException {
    /**
     * @deprecated BizException 은 클라이언트와 통신할 코드 (responseCode)가 추가로 필요하기 때문에 이 생성자는 사용하지 않습니다. 대신 {@link #BizException(String, String, Integer)}를 사용해주세요.
     */
    @Deprecated
    private BizException(String title) {
        this(title, null);
    }

    /**
     * @deprecated BizException 은 클라이언트와 통신할 코드 (responseCode)가 추가로 필요하기 때문에 이 생성자는 사용하지 않습니다. 대신 {@link #BizException(String, String, Integer)}를 사용해주세요.
     */
    @Deprecated
    private BizException(String title, String detail) {
        this(title, detail, (Map<String, Object>) null);
    }

    protected BizException(@Nullable String title, @Nullable String detail, Integer responseCode) {
        this(title, detail, Map.of("responseCode", responseCode));
    }

    protected BizException(@Nullable String title, @Nullable String detail, Map<String, Object> properties) {
        super(HttpStatus.OK);
        super.setTitle(title);
        super.setDetail(detail);
        super.getBody().setProperties(properties);
    }
}
