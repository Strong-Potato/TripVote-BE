package fc.be.app.common.authentication.model;

import com.auth0.jwt.exceptions.JWTDecodeException;

import java.time.Duration;

public class ModifyToken extends RandomKeyToken {
    private static final String DEFAULT_PREFIX = "modify_token_";
    private static final Duration DEFAULT_DURATION = Duration.ofMinutes(300);
    private final AuthenticationStrategy authenticationStrategy;
    private final String prefix = DEFAULT_PREFIX;
    private final Duration duration = DEFAULT_DURATION;

    public ModifyToken(String tokenValue, Object target, AuthenticationStrategy authenticationStrategy, boolean authenticated) {
        super(DEFAULT_PREFIX, tokenValue, target, authenticated);
        this.authenticationStrategy = authenticationStrategy;
    }

    public AuthenticationStrategy getAuthenticationStrategy() {
        return authenticationStrategy;
    }

    public static ModifyToken unauthenticated(String tokenValue, Object target, AuthenticationStrategy authenticationStrategy) throws JWTDecodeException {
        return new ModifyToken(tokenValue, target, authenticationStrategy, false);
    }

    public static ModifyToken authenticated(String tokenValue, Object target, AuthenticationStrategy authenticationStrategy) {
        return new ModifyToken(tokenValue, target, authenticationStrategy, true);
    }

    @Override
    public String getPrefix() {
        return prefix;
    }

    public Duration getDuration() {
        return duration;
    }

    public enum AuthenticationStrategy {
        ID, EMAIL
    }
}
