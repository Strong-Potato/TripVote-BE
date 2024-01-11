package fc.be.app.global.exception;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
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
    public ResponseEntity handleException(AccessDeniedException ex, WebRequest request) {
        HttpStatus httpStatus = HttpStatus.FORBIDDEN;
        String errorMessage = "Not permitted request";
        ProblemDetail problemDetail = createProblemDetail(ex, httpStatus, errorMessage, null, null, request);
        problemDetail.setType(type);
        return handleExceptionInternal(ex, problemDetail, new HttpHeaders(), httpStatus, request);
    }

    @ExceptionHandler
    public ResponseEntity handleException(AuthenticationException ex, WebRequest request) {
        HttpStatus httpStatus = HttpStatus.UNAUTHORIZED;
        String errorMessage = "Authentication Failed";
        if (ex instanceof OAuth2AuthenticationException exception) {
            log.warn("[OAUTH_AUTHENTICATION_EXCEPTION] OAUTH_AUTHENTICATION FAILED : {}", exception.getMessage(), exception.getCause());
            errorMessage = exception.getMessage();
        } else if (ex instanceof AuthenticationException exception) {
            log.warn("[AUTHENTICATION_EXCEPTION] AUTHENTICATION FAILED : {}", exception.getMessage(), exception.getCause());
            switch (exception) {
                case BadCredentialsException
                        badCredentialsException -> errorMessage = "이메일 또는 비밀번호를 확인해주세요.";
                case InsufficientAuthenticationException
                        insufficientAuthenticationException -> errorMessage = "로그인을 먼저 해주세요.";
                case UsernameNotFoundException
                        usernameNotFoundException -> errorMessage = "회원가입되지 않은 이메일입니다.";
                default -> {
                }
            }
        }
        ProblemDetail problemDetail = createProblemDetail(ex, httpStatus, errorMessage, null, null, request);
        problemDetail.setType(type);
        return handleExceptionInternal(ex, problemDetail, new HttpHeaders(), httpStatus, request);
    }

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
