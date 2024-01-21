package fc.be.app.common.authentication.model;

import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTDecodeException;

public class JoinSpaceToken extends JwtToken {
    private final String issuer;
    private final Long targetSpaceId;

    protected JoinSpaceToken(String tokenValue, String issuer, Long targetSpaceId, boolean authenticated) {
        super(tokenValue != null ? JWT.decode(tokenValue) : null, authenticated);
        this.issuer = issuer;
        this.targetSpaceId = targetSpaceId;
    }

    public static JoinSpaceToken unauthenticated(String tokenValue, String issuer, Long targetSpaceId) throws JWTDecodeException {
        return new JoinSpaceToken(tokenValue, issuer, targetSpaceId, false);
    }

    public static JoinSpaceToken authenticated(String issuer, Long targetSpaceId) {
        return new JoinSpaceToken(null, issuer, targetSpaceId, true);
    }

    public String getIssuer() {
        return issuer;
    }

    public Long getTargetSpaceId() {
        return targetSpaceId;
    }
}
