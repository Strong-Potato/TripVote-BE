package fc.be.app.common.authentication.manager;

import fc.be.app.common.authentication.model.Token;

public interface TokenManager {
    Token generate(Token token);

    Token authenticate(Token token);

    void remove(Token token);

    boolean supports(Class<? extends Token> token);
}
