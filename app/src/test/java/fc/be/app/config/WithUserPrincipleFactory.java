package fc.be.app.config;

import fc.be.app.global.config.security.model.token.LoginAuthenticationToken;
import fc.be.app.global.config.security.model.user.UserPrincipal;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import java.util.Collections;

public class WithUserPrincipleFactory implements WithSecurityContextFactory<WithUserPrinciple> {
    @Override
    public SecurityContext createSecurityContext(WithUserPrinciple customUserPrinciple) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();

        UserPrincipal principal = new UserPrincipal(customUserPrinciple.id(),
                customUserPrinciple.email(),
                customUserPrinciple.authProvider());

        Authentication auth =
                LoginAuthenticationToken.authenticated(principal, "password", Collections.emptyList());
        context.setAuthentication(auth);
        return context;
    }
}
