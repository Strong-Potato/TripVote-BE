package fc.be.openapi.tourapi.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Deprecated(forRemoval = true)
@RestControllerAdvice
public class TourAPIExceptionHandler extends ProblemDetailCreator<TourAPIErrorMessage> {
    protected TourAPIExceptionHandler() {
        super("TourAPI 처리 실패");
    }

    @ExceptionHandler(NoItemsFromAPIException.class)
    public ProblemDetail handleNotGatheredAccommodationsFromAPIException(HttpServletRequest request) {
        return createProblemDetail(TourAPIErrorMessage.NO_ITEMS_FROM_API, request);
    }

    @ExceptionHandler(WrongXMLFormatException.class)
    public ProblemDetail handleWrongXMLFormatException(
            WrongXMLFormatException ex,
            HttpServletRequest request) {
        return createProblemDetail(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value(), request);
    }

    @ExceptionHandler(WrongRequestException.class)
    public ProblemDetail handleWrongCallBackException(WrongRequestException ex, HttpServletRequest request) {
        return createProblemDetail(TourAPIErrorMessage.valueOf(ex.getMessage()), request);
    }
}