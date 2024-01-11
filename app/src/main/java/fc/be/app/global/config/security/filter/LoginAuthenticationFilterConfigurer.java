package fc.be.app.global.config.security.filter;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractAuthenticationFilterConfigurer;
import org.springframework.security.oauth2.client.web.OAuth2LoginAuthenticationFilter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

public class LoginAuthenticationFilterConfigurer
        extends AbstractAuthenticationFilterConfigurer<HttpSecurity, LoginAuthenticationFilterConfigurer, LoginAuthenticationFilter> {
    private AuthenticationSuccessHandler successHandler;
    private AuthenticationFailureHandler failureHandler;
    private AuthenticationDetailsSource<HttpServletRequest, ?> authenticationDetailsSource;
    private SessionAuthenticationStrategy sessionStrategy;

    public LoginAuthenticationFilterConfigurer(LoginAuthenticationFilter authenticationFilter, String defaultLoginProcessingUrl) {
        super(authenticationFilter, defaultLoginProcessingUrl);
    }

    @Override
    public void init(HttpSecurity http) throws Exception {
        super.init(http);
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        LoginAuthenticationFilter filter = getAuthenticationFilter();
        AuthenticationManager authenticationManager = http.getSharedObject(AuthenticationManager.class);
        filter.setAuthenticationManager(authenticationManager);
        filter.setAuthenticationSuccessHandler(successHandler);
        filter.setAuthenticationFailureHandler(failureHandler);
        filter.setAuthenticationDetailsSource(authenticationDetailsSource);
        if (sessionStrategy != null) {
            filter.setSessionAuthenticationStrategy(sessionStrategy);
        } else {
            SessionAuthenticationStrategy sessionAuthenticationStrategy = http.getSharedObject(SessionAuthenticationStrategy.class);
            filter.setSessionAuthenticationStrategy(sessionAuthenticationStrategy);
        }
        http.setSharedObject(LoginAuthenticationFilter.class, getAuthenticationFilter());
        http.addFilterAfter(getAuthenticationFilter(), OAuth2LoginAuthenticationFilter.class);
    }

    public LoginAuthenticationFilterConfigurer successHandlerLogin(AuthenticationSuccessHandler successHandler) {
        this.successHandler = successHandler;
        return this;
    }

    public LoginAuthenticationFilterConfigurer failureHandlerLogin(AuthenticationFailureHandler authenticationFailureHandler) {
        this.failureHandler = authenticationFailureHandler;
        return this;
    }

    public LoginAuthenticationFilterConfigurer setAuthenticationDetailsSource(AuthenticationDetailsSource<HttpServletRequest, ?> authenticationDetailsSource) {
        this.authenticationDetailsSource = authenticationDetailsSource;
        return this;
    }

    public LoginAuthenticationFilterConfigurer setSessionStrategy(SessionAuthenticationStrategy sessionStrategy) {
        this.sessionStrategy = sessionStrategy;
        return this;
    }

    @Override
    protected RequestMatcher createLoginProcessingUrlMatcher(String loginProcessingUrl) {
        return new AntPathRequestMatcher(loginProcessingUrl, "POST");
    }

    public static LoginAuthenticationFilterConfigurer loginAuthenticationFilter(LoginAuthenticationFilter authenticationFilter, String defaultLoginProcessingUrl) {
        return new LoginAuthenticationFilterConfigurer(authenticationFilter, defaultLoginProcessingUrl);
    }
}
