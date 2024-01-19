package fc.be.app.common.authentication.manager;

import fc.be.app.common.authentication.exception.AuthException;
import fc.be.app.common.authentication.model.Token;
import fc.be.app.common.authentication.provider.TokenAuthenticationProvider;
import fc.be.app.global.config.security.exception.UnsupportedProviderException;
import fc.be.app.global.exception.InternalServiceErrorCode;
import fc.be.app.global.exception.InternalServiceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class TokenAuthenticationProviderManager implements TokenAuthenticationManager {
    private final List<TokenAuthenticationProvider> providers;

    public TokenAuthenticationProviderManager(List<TokenAuthenticationProvider> providers) {
        this.providers = providers;
    }

    @Override
    public Token authenticate(Token token) {
        Class<? extends Token> toTest = token.getClass();
        Token result;
        for (TokenAuthenticationProvider provider : getProviders()) {
            if (!provider.supports(toTest)) {
                continue;
            }
            try {
                result = provider.authenticate(token);
                if (result != null && result.isAuthenticated()) {
                    break;
                }
            } catch (AuthException exception) {
                throw exception;
            } catch (Exception exception) {
                log.error("critical error occurred while authenticating token");
                throw new InternalServiceException(InternalServiceErrorCode.UN_KNOWN, exception);
            }
        }
        throw new UnsupportedProviderException("TokenProviderManager에 등록되지 않은 토큰에 인증을 시도했습니다.");
    }

    private List<TokenAuthenticationProvider> getProviders() {
        return providers;
    }
}
