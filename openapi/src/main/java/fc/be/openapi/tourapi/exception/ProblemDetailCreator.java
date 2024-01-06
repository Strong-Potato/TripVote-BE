package fc.be.openapi.tourapi.exception;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;

import java.net.URI;

@Deprecated(forRemoval = true)
@Slf4j
public abstract class ProblemDetailCreator<T extends ErrorMessage> {
    private final String title;

    protected ProblemDetailCreator(String title) {
        this.title = title;
    }

    public ProblemDetail createProblemDetail(T errorMessage, HttpServletRequest request) {
        return createProblemDetail(errorMessage.getMessage(), errorMessage.getStatus().value(), request);
    }

    public ProblemDetail createProblemDetail(String errorMessage, int status, HttpServletRequest request) {
        log.error("error: {}| detail: {}| status: {}", title, errorMessage, status);

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatusCode.valueOf(status),
                errorMessage
        );

        problemDetail.setType(URI.create(request.getRequestURL().toString()));
        problemDetail.setTitle(title);
        return problemDetail;
    }
}