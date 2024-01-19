package fc.be.app.common.authentication.manager;

import fc.be.app.common.authentication.model.Token;

public interface TokenAuthenticationManager {
    Token authenticate(Token token);
}
