package fc.be.global.exception;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.net.URI;
import java.util.Map;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    private static final URI type = URI.create("https://tripvote.site/error");

    @ExceptionHandler
    public ResponseEntity handleException(ExternalServiceException ex, WebRequest request) {
        log.error("[CRITICAL] EXTERNAL SERVICE ERROR : {}", ex.getCause());

        ex.setType(type);

        ProblemDetail body = ex.getBody();
        addRequestedProperties(body, request);

        return handleExceptionInternal(ex, ex.getBody(), ex.getHeaders(), ex.getStatusCode(), request);
    }

    @ExceptionHandler
    public ResponseEntity handleException(BizException ex, NativeWebRequest request) {
        HttpServletRequest servletRequest = request.getNativeRequest(HttpServletRequest.class);
        ServletServerHttpRequest servletServerHttpRequest = new ServletServerHttpRequest(servletRequest);
        String requestURI = servletServerHttpRequest.getServletRequest().getRequestURI();
        log.warn("[BIZ_EXCEPTION] request uri : {}", requestURI);

        ex.setType(type);

        ProblemDetail body = ex.getBody();
        addRequestedProperties(body, request);

        return handleExceptionInternal(ex, ex.getBody(), ex.getHeaders(), ex.getStatusCode(), request);
    }

    private Map<String, Object> addRequestedProperties(ProblemDetail body, WebRequest request) {
        Map<String, Object> properties = body.getProperties();

        for (Map.Entry<String, String[]> entry : request.getParameterMap().entrySet()) {
            String parameters = String.join(", ", entry.getValue());
            properties.put(entry.getKey(), parameters);
        }

        return properties;
    }
}
