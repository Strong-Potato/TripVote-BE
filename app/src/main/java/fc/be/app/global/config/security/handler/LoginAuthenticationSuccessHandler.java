package fc.be.app.global.config.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import fc.be.app.global.config.security.model.token.LoginAuthenticationToken;
import fc.be.app.global.config.security.model.user.UserPrincipal;
import fc.be.app.global.config.security.properties.TokenProperties;
import fc.be.app.global.config.security.provider.AuthenticationDetails;
import fc.be.app.global.config.security.service.RefreshTokenService;
import fc.be.app.global.http.ApiResponse;
import fc.be.app.global.util.CookieUtil;
import fc.be.app.global.util.JwtService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class LoginAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;
    private final TokenProperties tokenProperties;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public LoginAuthenticationSuccessHandler(JwtService jwtService, RefreshTokenService refreshTokenService, TokenProperties tokenProperties) {
        this.jwtService = jwtService;
        this.refreshTokenService = refreshTokenService;
        this.tokenProperties = tokenProperties;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        LoginAuthenticationToken loginAuthentication = (LoginAuthenticationToken) authentication;

        // get userPrincipal
        UserPrincipal principal = (UserPrincipal) loginAuthentication.getPrincipal();

        // response
        String accessToken = jwtService.generateAccessToken(principal);
        CookieUtil.addCookie(response, tokenProperties.getAccessTokenName(), accessToken, Integer.parseInt(tokenProperties.getAccessTokenCookieExpireTime()));

        String refreshToken = refreshTokenService.refresh(accessToken, principal, (AuthenticationDetails) loginAuthentication.getDetails());
        CookieUtil.addCookie(response, tokenProperties.getRefreshTokenName(), refreshToken, Integer.parseInt(tokenProperties.getRefreshTokenCookieExpireTime()));

        response.setStatus(HttpStatus.OK.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");

        objectMapper.writeValue(response.getWriter(), ApiResponse.ok());
    }
}
