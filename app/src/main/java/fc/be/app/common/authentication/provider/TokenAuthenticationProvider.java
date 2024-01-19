package fc.be.app.common.authentication.provider;

import fc.be.app.common.authentication.model.Token;

public interface TokenAuthenticationProvider {
    Token authenticate(Token token);

    boolean supports(Class<? extends Token> token);
}
