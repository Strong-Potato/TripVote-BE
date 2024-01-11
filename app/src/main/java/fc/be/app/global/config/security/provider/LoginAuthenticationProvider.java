package fc.be.app.global.config.security.provider;

import fc.be.app.domain.member.vo.AuthProvider;
import fc.be.app.global.config.security.model.LoginUser;
import fc.be.app.global.config.security.model.UserPrincipal;
import fc.be.app.global.config.security.provider.token.LoginAuthenticationToken;
import fc.be.app.global.config.security.service.LoginUserDetailService;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class LoginAuthenticationProvider implements AuthenticationProvider {
    private final LoginUserDetailService userDetailService;
    private final PasswordEncoder passwordEncoder;

    public LoginAuthenticationProvider(LoginUserDetailService userDetailService, PasswordEncoder passwordEncoder) {
        this.userDetailService = userDetailService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String password = (String) authentication.getCredentials();
        LoginUser loginUser = (LoginUser) userDetailService.loadUserByUsername(username);
        if (!passwordEncoder.matches(password, loginUser.getPassword())) {
            throw new BadCredentialsException("BadCredentialsException");
        }
        UserPrincipal userPrincipal = new UserPrincipal(loginUser.getId(), loginUser.getUsername(), AuthProvider.NONE);
        LoginAuthenticationToken result =
                LoginAuthenticationToken.authenticated(userPrincipal, null, loginUser.getAuthorities());
        result.setDetails(authentication.getDetails());
        return result;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return LoginAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
