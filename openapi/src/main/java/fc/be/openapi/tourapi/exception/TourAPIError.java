package fc.be.openapi.tourapi.exception;

import fc.be.openapi.tourapi.tools.ErrorMessageLoader;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
public class TourAPIError {
    private static final ErrorMessageLoader errorMessageLoader;
    private final String message;

    static {
        errorMessageLoader = new ErrorMessageLoader("error-openapi.yml");
    }

    public TourAPIError(TourAPIErrorCode errorCode) {
        this.message = errorMessageLoader.getErrorMessage(errorCode);

        log.error(TourAPIError.class.getSimpleName() + ": {}", message);
    }

    public TourAPIError(TourAPIErrorCode errorCode, Object... objs) {
        this.message = errorMessageLoader.getErrorMessage(errorCode, objs);

        log.error(TourAPIError.class.getSimpleName() + ": {}", message);
    }
}