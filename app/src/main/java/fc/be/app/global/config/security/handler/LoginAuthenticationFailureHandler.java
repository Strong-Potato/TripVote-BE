package fc.be.app.global.config.security.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;

public class LoginAuthenticationFailureHandler implements AuthenticationFailureHandler {
    private final HandlerExceptionResolver exceptionHandler;

    public LoginAuthenticationFailureHandler(HandlerExceptionResolver exceptionHandler) {
        this.exceptionHandler = exceptionHandler;
    }

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        exceptionHandler.resolveException(request, response, null, exception);
    }
}
