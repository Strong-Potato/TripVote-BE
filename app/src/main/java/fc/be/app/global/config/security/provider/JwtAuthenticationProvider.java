package fc.be.app.global.config.security.provider;

import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import fc.be.app.global.config.security.exception.InvalidJwtException;
import fc.be.app.global.config.security.model.token.JwtAuthenticationToken;
import fc.be.app.global.config.security.model.token.TokenPair;
import fc.be.app.global.config.security.model.user.UserPrincipal;
import fc.be.app.global.config.security.properties.TokenProperties;
import fc.be.app.global.config.security.service.RefreshTokenService;
import fc.be.app.global.util.JwtService;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class JwtAuthenticationProvider implements AuthenticationProvider {
    private final RefreshTokenService refreshTokenService;
    private final JwtService jwtService;
    private final TokenProperties tokenProperties;

    public JwtAuthenticationProvider(RefreshTokenService refreshTokenService, JwtService jwtService, TokenProperties tokenProperties) {
        this.refreshTokenService = refreshTokenService;
        this.jwtService = jwtService;
        this.tokenProperties = tokenProperties;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        JwtAuthenticationToken jwtAuthentication = (JwtAuthenticationToken) authentication;
        TokenPair tokenPair = (TokenPair) jwtAuthentication.getCredentials();
        AuthenticationDetails details = (AuthenticationDetails) authentication.getDetails();
        JwtAuthenticationToken authResult = null;
        try {
            DecodedJWT decodedJwt = jwtService.verify(tokenPair.accessToken());
            UserPrincipal userPrincipal =
                    decodedJwt.getClaim(tokenProperties.getAccessTokenAuthenticationProperty()).as(UserPrincipal.class);
            List<SimpleGrantedAuthority> authorities =
                    decodedJwt.getClaim(tokenProperties.getAccessTokenAuthoritiesProperty()).asList(SimpleGrantedAuthority.class);
            authResult = JwtAuthenticationToken.authenticated(userPrincipal, null, authorities);
        } catch (TokenExpiredException ex) {
            DecodedJWT decodedJwt = JWT.decode(tokenPair.accessToken());
            UserPrincipal userPrincipal =
                    decodedJwt.getClaim(tokenProperties.getAccessTokenAuthenticationProperty()).as(UserPrincipal.class);
            List<SimpleGrantedAuthority> authorities =
                    decodedJwt.getClaim(tokenProperties.getAccessTokenAuthoritiesProperty()).asList(SimpleGrantedAuthority.class);
            boolean isValidRefreshToken = refreshTokenService.isValid(tokenPair, userPrincipal, details);
            if (isValidRefreshToken) {
                String newAccessToken = jwtService.generateAccessToken(userPrincipal, authorities);
                TokenPair newTokenPair = new TokenPair(newAccessToken, tokenPair.refreshToken(), true);
                authResult = JwtAuthenticationToken.authenticated(userPrincipal, newTokenPair, authorities);
                authResult.setDetails(authentication.getDetails());
            } else {
                authResult = null;
            }
        } catch (JWTVerificationException ex) {
            throw new InvalidJwtException(ex.getMessage(), ex);
        }
        return authResult;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return JwtAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
