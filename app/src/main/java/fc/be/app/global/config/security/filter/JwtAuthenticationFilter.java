package fc.be.app.global.config.security.filter;

import fc.be.app.global.config.security.model.token.JwtAuthenticationToken;
import fc.be.app.global.config.security.model.token.TokenPair;
import fc.be.app.global.config.security.properties.TokenProperties;
import fc.be.app.global.config.security.provider.JwtAuthenticationProvider;
import fc.be.app.global.util.CookieUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.log.LogMessage;
import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private SecurityContextHolderStrategy securityContextHolderStrategy = SecurityContextHolder.getContextHolderStrategy();

    private final JwtAuthenticationProvider jwtAuthenticationProvider;
    private final AuthenticationDetailsSource<HttpServletRequest, ?> authenticationDetailsSource;
    private final TokenProperties tokenProperties;

    public JwtAuthenticationFilter(JwtAuthenticationProvider jwtAuthenticationProvider, AuthenticationDetailsSource<HttpServletRequest, ?> authenticationDetailsSource, TokenProperties tokenProperties) {
        this.jwtAuthenticationProvider = jwtAuthenticationProvider;
        this.authenticationDetailsSource = authenticationDetailsSource;
        this.tokenProperties = tokenProperties;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        if (this.securityContextHolderStrategy.getContext().getAuthentication() != null) {
            this.logger.debug(LogMessage
                    .of(() -> "SecurityContextHolder not populated with jwt token, as it already contained: '"
                            + this.securityContextHolderStrategy.getContext().getAuthentication() + "'"));
            chain.doFilter(request, response);
            return;
        }
        Optional<Cookie> accessToken = CookieUtil.getCookie(request, tokenProperties.getAccessTokenName());
        Optional<Cookie> refreshToken = CookieUtil.getCookie(request, tokenProperties.getRefreshTokenName());
        if (accessToken.isPresent()) {
            String accessTokenValue = accessToken.map(Cookie::getValue).orElse("");
            String refreshTokenValue = refreshToken.map(Cookie::getValue).orElse("");
            TokenPair tokenPair = new TokenPair(accessTokenValue, refreshTokenValue, false);
            JwtAuthenticationToken jwtAuth = JwtAuthenticationToken.unauthenticated(null, tokenPair);
            setDetails(request, jwtAuth);
            // Attempt authentication via AuthenticationManager
            try {
                Authentication authResult = this.jwtAuthenticationProvider.authenticate(jwtAuth);
                if (authResult == null) {
                    // return immediately as subclass has indicated that it hasn't completed
                    return;
                }
                SecurityContext context = this.securityContextHolderStrategy.createEmptyContext();
                context.setAuthentication(authResult);
                this.securityContextHolderStrategy.setContext(context);
                onSuccessfulAuthentication(request, response, authResult);
                this.logger.debug(
                        LogMessage.of(() -> "SecurityContextHolder populated with jwt token: '"
                                + this.securityContextHolderStrategy.getContext().getAuthentication()
                                + "'"));
            } catch (AuthenticationException ex) {
                this.logger.debug(LogMessage
                                .format(
                                        "SecurityContextHolder not populated with jwt token, "
                                                + "as AuthenticationManager rejected Authentication returned "
                                                + "by JwtProvider: '%s'; "
                                                + "invalidating jwt token", jwtAuth),
                        ex);
                onUnsuccessfulAuthentication(request, response, ex);
            }
        }
        chain.doFilter(request, response);
    }

    protected void setDetails(HttpServletRequest request, JwtAuthenticationToken authRequest) {
        authRequest.setDetails(this.authenticationDetailsSource.buildDetails(request));
    }

    protected void onSuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, Authentication authResult) {
        if (authResult instanceof JwtAuthenticationToken jwtAuthToken && jwtAuthToken.getCredentials() != null) {
            TokenPair newTokenPair = (TokenPair) jwtAuthToken.getCredentials();
            if (newTokenPair.isRegenerated()) {
                CookieUtil.addCookie(response, tokenProperties.getAccessTokenName(), newTokenPair.accessToken(), Integer.parseInt(tokenProperties.getAccessTokenCookieExpireTime()));
            }
        }
    }

    protected void onUnsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) {

    }
}
