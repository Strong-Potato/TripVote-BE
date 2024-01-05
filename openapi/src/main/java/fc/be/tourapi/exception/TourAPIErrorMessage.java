package fc.be.tourapi.exception;

import fc.be.tourapi.exception.ErrorMessage;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.SERVICE_UNAVAILABLE;

@Deprecated(forRemoval = true)
@Getter
@AllArgsConstructor
public enum TourAPIErrorMessage implements ErrorMessage {
    // Tour API 요청 오류
    NO_ITEMS_FROM_API(NO_CONTENT, "결과가 반환된 것이 없습니다"),

    // Tour API 공통 오류
    APPLICATION_ERROR(SERVICE_UNAVAILABLE, "Tour API 서버 어플리케이션 에러입니다"),
    HTTP_ERROR(SERVICE_UNAVAILABLE, "Tour API 서버 HTTP 에러입니다"),
    NO_OPENAPI_SERVICE_ERROR(SERVICE_UNAVAILABLE, "해당 Tour API 서비스가 없거나 폐기되었습니다"),
    SERVICE_ACCESS_DENIED_ERROR(SERVICE_UNAVAILABLE, "Tour API 서비스 접근이 거부되었습니다"),
    LIMITED_NUMBER_OF_SERVICE_REQUESTS_EXCEEDS_ERROR(SERVICE_UNAVAILABLE, "Tour API 서비스 요청 제한 횟수를 초과하였습니다"),
    SERVICE_KEY_IS_NOT_REGISTERED_ERROR(SERVICE_UNAVAILABLE, "등록되지 않은 Tour API 서비스 키입니다"),
    DEADLINE_HAS_EXPIRED_ERROR(SERVICE_UNAVAILABLE, "Tour API 서비스 활용 기간이 만료되었습니다"),
    UNREGISTERED_IP_ERROR(SERVICE_UNAVAILABLE, "등록되지 않은 Tour API 서비스 IP입니다"),
    UNKNOWN_ERROR(SERVICE_UNAVAILABLE, "Tour API 서버 알 수 없는 에러입니다");

    private final HttpStatus status;
    private final String message;
}
