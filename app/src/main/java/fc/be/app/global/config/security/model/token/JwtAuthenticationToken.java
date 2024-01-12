package fc.be.app.global.config.security.model.token;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class JwtAuthenticationToken extends AbstractAuthenticationToken {
    private final Object principal;
    private final TokenPair tokenPair;

    public JwtAuthenticationToken(Object principal, TokenPair tokenPair) {
        super(null);
        this.principal = principal;
        this.tokenPair = tokenPair;
        setAuthenticated(false);
    }

    public JwtAuthenticationToken(Object principal, TokenPair tokenPair, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.principal = principal;
        this.tokenPair = tokenPair;
        super.setAuthenticated(true); // must use super, as we override
    }

    public static JwtAuthenticationToken unauthenticated(Object principal, TokenPair tokenPair) {
        return new JwtAuthenticationToken(principal, tokenPair);
    }

    public static JwtAuthenticationToken authenticated(Object principal, TokenPair tokenPair, Collection<? extends GrantedAuthority> authorities) {
        return new JwtAuthenticationToken(principal, tokenPair, authorities);
    }

    @Override
    public Object getPrincipal() {
        return this.principal;
    }

    @Override
    public Object getCredentials() {
        return this.tokenPair;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        super.setAuthenticated(false);
    }

    @Override
    public void eraseCredentials() {
        super.eraseCredentials();
    }
}
