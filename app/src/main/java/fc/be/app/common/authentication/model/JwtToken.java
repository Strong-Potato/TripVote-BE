package fc.be.app.common.authentication.model;

import com.auth0.jwt.interfaces.DecodedJWT;

public abstract class JwtToken implements Token {
    private final DecodedJWT decodedJWT;
    private boolean authenticated;

    public JwtToken(DecodedJWT decodedJWT, boolean authenticated) {
        this.decodedJWT = decodedJWT;
        this.authenticated = authenticated;
    }

    @Override
    public String getTokenValue() {
        return decodedJWT.getToken();
    }

    @Override
    public boolean isAuthenticated() {
        return this.authenticated;
    }
}
