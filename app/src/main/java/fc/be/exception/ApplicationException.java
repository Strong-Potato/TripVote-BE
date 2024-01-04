package fc.be.exception;

import org.springframework.http.HttpStatusCode;
import org.springframework.web.ErrorResponseException;

public class ApplicationException extends ErrorResponseException {

    public ApplicationException(HttpStatusCode status) {
        super(status);
    }
}
