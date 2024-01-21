package fc.be.app.common.authentication.model;

import com.auth0.jwt.exceptions.JWTDecodeException;

import java.time.Duration;

public class RegisterToken extends RandomKeyToken {
    private static final String DEFAULT_PREFIX = "register_token_";
    private static final Duration DEFAULT_DURATION = Duration.ofMinutes(300);

    private final String prefix = DEFAULT_PREFIX;
    private final Duration duration = DEFAULT_DURATION;

    public RegisterToken(String tokenValue, String targetEmail, boolean authenticated) {
        super(DEFAULT_PREFIX, tokenValue, targetEmail, authenticated);
    }

    public static RegisterToken unauthenticated(String tokenValue, String targetEmail) throws JWTDecodeException {
        return new RegisterToken(tokenValue, targetEmail, false);
    }

    public static RegisterToken authenticated(String tokenValue, String targetEmail) {
        return new RegisterToken(tokenValue, targetEmail, true);
    }

    @Override
    public String getPrefix() {
        return prefix;
    }

    public java.time.Duration getDuration() {
        return duration;
    }
}
