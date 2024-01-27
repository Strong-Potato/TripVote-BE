package fc.be.app.global.config.security.handler;

import com.auth0.jwt.exceptions.JWTCreationException;
import fc.be.app.domain.member.service.MemberQuery;
import fc.be.app.domain.member.vo.AuthProvider;
import fc.be.app.global.config.security.converter.Converter;
import fc.be.app.global.config.security.converter.ProviderUserConvertRequest;
import fc.be.app.global.config.security.model.user.ProviderUser;
import fc.be.app.global.config.security.model.user.UserPrincipal;
import fc.be.app.global.config.security.properties.TokenProperties;
import fc.be.app.global.config.security.provider.AuthenticationDetails;
import fc.be.app.global.config.security.service.RefreshTokenService;
import fc.be.app.global.util.CookieUtil;
import fc.be.app.global.util.JwtService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class OAuth2AuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    private final Converter<ProviderUserConvertRequest, ProviderUser> converter;
    private final MemberQuery memberQuery;
    private final JwtService jwtService;
    private final ClientRegistrationRepository clientRegistrationRepository;
    private final RefreshTokenService refreshTokenService;
    private final TokenProperties tokenProperties;

    public OAuth2AuthenticationSuccessHandler(MemberQuery memberQuery, Converter<ProviderUserConvertRequest, ProviderUser> converter, JwtService jwtService, ClientRegistrationRepository clientRegistrationRepository, RefreshTokenService refreshTokenService, TokenProperties tokenProperties) {
        this.memberQuery = memberQuery;
        this.converter = converter;
        this.jwtService = jwtService;
        this.clientRegistrationRepository = clientRegistrationRepository;
        this.refreshTokenService = refreshTokenService;
        this.tokenProperties = tokenProperties;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        OAuth2AuthenticationToken oauth2Authentication = (OAuth2AuthenticationToken) authentication;
        // get clientRegistration
        String authorizedClientRegistrationId = oauth2Authentication.getAuthorizedClientRegistrationId();
        ClientRegistration clientRegistration = clientRegistrationRepository.findByRegistrationId(authorizedClientRegistrationId);

        // get principal
        OAuth2User principal = oauth2Authentication.getPrincipal();
        ProviderUserConvertRequest convertRequest = new ProviderUserConvertRequest(clientRegistration, principal);
        ProviderUser providerUser = converter.convert(convertRequest);
        String email = providerUser.getEmail();
        AuthProvider authProvider = AuthProvider.of(authorizedClientRegistrationId);

        // get member
        MemberQuery.ProviderMemberRequest providerMemberRequest = new MemberQuery.ProviderMemberRequest(authProvider, email);
        MemberQuery.MemberResponse memberResponse = memberQuery.find(providerMemberRequest).orElseThrow();

        // convert to userPrincipal
        UserPrincipal userPrincipal = new UserPrincipal(memberResponse.id(), memberResponse.email(), memberResponse.provider());

        // response
        String accessToken;
        try {
            accessToken = jwtService.generateAccessToken(userPrincipal);
        } catch (JWTCreationException ex) {
            throw new InternalAuthenticationServiceException(ex.getMessage(), ex);
        }
        CookieUtil.addCookie(response, tokenProperties.getAccessTokenName(), accessToken, Integer.parseInt(tokenProperties.getAccessTokenExpireTime()));
        CookieUtil.addCookieForLocal(response, tokenProperties.getAccessTokenName(), accessToken, Integer.parseInt(tokenProperties.getAccessTokenExpireTime()));

        String refreshToken = refreshTokenService.refresh(accessToken, userPrincipal, (AuthenticationDetails) oauth2Authentication.getDetails());
        CookieUtil.addCookie(response, tokenProperties.getRefreshTokenName(), refreshToken, Integer.parseInt(tokenProperties.getRefreshTokenCookieExpireTime()));
        CookieUtil.addCookieForLocal(response, tokenProperties.getRefreshTokenName(), refreshToken, Integer.parseInt(tokenProperties.getRefreshTokenCookieExpireTime()));

        CookieUtil.addSessionCookie(response, "isLogin", "true");
        CookieUtil.addSessionCookieForLocal(response, "isLogin", "true");

        response.sendRedirect("https://tripvote.site");
    }
}
