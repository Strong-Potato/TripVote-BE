package fc.be.app.config;

import fc.be.app.domain.member.vo.AuthProvider;
import org.springframework.security.test.context.support.WithSecurityContext;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = WithUserPrincipleFactory.class)
public @interface WithUserPrinciple {

    long id() default 1;

    String email() default "test@email.com";

    AuthProvider authProvider() default AuthProvider.NONE;
}
