
package fc.be.openapi.tourapi.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TourAPIErrorCode {

    // 타입 매핑 오류
    NO_CONTENTTPYEID,
    IMAGE_ERROR,

    // 요청 오류
    NO_ITEMS_FROM_API,

    // 통신 오류,
    APPLICATION_ERROR,
    HTTP_ERROR,
    NO_OPENAPI_SERVICE,
    SERVICE_ACCESS_DENIED,
    LIMITED_NUMBER_OF_SERVICE_REQUESTS_EXCEEDS,
    SERVICE_KEY_IS_NOT_REGISTERED,
    DEADLINE_HAS_EXPIRED,
    UNREGISTERED_IP,
    UNKNOWN,
    COMMUNICATOR_WRONG_REQUEST,
    WRONG_XML_FORMAT,

    // Tools 오류
    OBJECT_TYPE_ERROR,
    CLASS_CAST_ERROR,
    JSON_PARSING_ERROR
}
