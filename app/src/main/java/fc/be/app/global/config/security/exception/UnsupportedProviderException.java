package fc.be.app.global.config.security.exception;

import org.springframework.security.authentication.InternalAuthenticationServiceException;

public class UnsupportedProviderException extends InternalAuthenticationServiceException {
    public UnsupportedProviderException(String message, Throwable cause) {
        super(message, cause);
    }

    public UnsupportedProviderException(String message) {
        super(message);
    }
}
