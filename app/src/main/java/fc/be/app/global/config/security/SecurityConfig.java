package fc.be.app.global.config.security;

import fc.be.app.domain.member.vo.AuthProvider;
import fc.be.app.global.config.security.exception.RestAccessDeniedHandler;
import fc.be.app.global.config.security.exception.RestAuthenticationEntryPoint;
import fc.be.app.global.config.security.filter.JwtAuthenticationFilter;
import fc.be.app.global.config.security.filter.LoginAuthenticationFilter;
import fc.be.app.global.config.security.filter.LoginAuthenticationFilterConfigurer;
import fc.be.app.global.config.security.handler.LoginAuthenticationFailureHandler;
import fc.be.app.global.config.security.handler.LoginAuthenticationSuccessHandler;
import fc.be.app.global.config.security.handler.OAuth2AuthenticationFailureHandler;
import fc.be.app.global.config.security.handler.OAuth2AuthenticationSuccessHandler;
import fc.be.app.global.config.security.model.user.UserPrincipal;
import fc.be.app.global.config.security.provider.LoginAuthenticationProvider;
import fc.be.app.global.config.security.service.CustomOAuth2UserService;
import fc.be.app.global.config.security.service.CustomOidcUserService;
import jakarta.servlet.DispatcherType;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.annotation.web.configurers.FormLoginConfigurer;
import org.springframework.security.config.annotation.web.configurers.HttpBasicConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.client.oidc.web.logout.OidcClientInitiatedLogoutSuccessHandler;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.web.servlet.HandlerExceptionResolver;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final CustomOAuth2UserService customOAuth2UserService;
    private final CustomOidcUserService customOidcUserService;
    private final AuthenticationDetailsSource<HttpServletRequest, WebAuthenticationDetails> authenticationDetailsSource;
    private final LoginAuthenticationProvider loginAuthenticationProvider;
    private final HandlerExceptionResolver handlerExceptionResolver;
    private final ClientRegistrationRepository clientRegistrationRepository;
    private final LoginAuthenticationSuccessHandler loginAuthenticationSuccessHandler;
    private final OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    private static final String LOGIN_PROC_URL = "/login";
    private static final String FRONT_ROOT_URL = "https://tripvote.site";
    private static final String[] TOKEN_COOKIE_KEY = {"access-token", "refresh-token"};

    @Autowired
    public void authenticationManagerConfigure(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(loginAuthenticationProvider);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        // 기본 보안 설정
        httpSecurity
                .csrf(CsrfConfigurer::disable);

        // 인증 필터
        httpSecurity
                .httpBasic(HttpBasicConfigurer::disable)
                .formLogin(FormLoginConfigurer::disable)
                .oauth2Login(oauth2Login -> oauth2Login
                        .userInfoEndpoint(userInfoEndpoint -> userInfoEndpoint
                                .userService(customOAuth2UserService)
                                .oidcUserService(customOidcUserService))
                        .authenticationDetailsSource(authenticationDetailsSource)
                        .successHandler(oAuth2AuthenticationSuccessHandler)
                        .failureHandler(new OAuth2AuthenticationFailureHandler(handlerExceptionResolver)))
                .with(new LoginAuthenticationFilterConfigurer(new LoginAuthenticationFilter(LOGIN_PROC_URL), LOGIN_PROC_URL), (login) -> login
                        .loginProcessingUrl(LOGIN_PROC_URL)
                        .setAuthenticationDetailsSource(authenticationDetailsSource)
                        .successHandlerLogin(loginAuthenticationSuccessHandler)
                        .failureHandlerLogin(new LoginAuthenticationFailureHandler(handlerExceptionResolver)))
                .anonymous(anonymous -> anonymous
                        .principal(new UserPrincipal(0L, "anonymous", AuthProvider.NONE)));

        // 세션 유지 필터
        httpSecurity
                .addFilterBefore(jwtAuthenticationFilter, AnonymousAuthenticationFilter.class)
                .sessionManagement(sessionManagement -> sessionManagement
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        // 인가 필터
        httpSecurity
                .authorizeHttpRequests(authRequest -> authRequest
                        .dispatcherTypeMatchers(DispatcherType.ERROR).permitAll()
                        .anyRequest().permitAll());

        // 예외 처리 필터
        httpSecurity
                .exceptionHandling(exceptionHandling -> exceptionHandling
                        .authenticationEntryPoint(new RestAuthenticationEntryPoint(handlerExceptionResolver))
                        .accessDeniedHandler(new RestAccessDeniedHandler(handlerExceptionResolver)));

        // 로그아웃 필터
        httpSecurity
                .logout(logout -> logout
                        .clearAuthentication(true)
                        .invalidateHttpSession(true)
                        .deleteCookies(TOKEN_COOKIE_KEY)
                        .logoutSuccessHandler(oidcLogoutSuccessHandler()));

        return httpSecurity.build();
    }

    private LogoutSuccessHandler oidcLogoutSuccessHandler() {
        OidcClientInitiatedLogoutSuccessHandler logoutSuccessHandler = new OidcClientInitiatedLogoutSuccessHandler(clientRegistrationRepository);
        logoutSuccessHandler.setPostLogoutRedirectUri(FRONT_ROOT_URL);
        return logoutSuccessHandler;
    }
}
