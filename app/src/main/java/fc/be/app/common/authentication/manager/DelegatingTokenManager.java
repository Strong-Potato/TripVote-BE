package fc.be.app.common.authentication.manager;

import fc.be.app.common.authentication.exception.AuthException;
import fc.be.app.common.authentication.model.Token;
import fc.be.app.global.config.security.exception.UnsupportedProviderException;
import fc.be.app.global.exception.InternalServiceErrorCode;
import fc.be.app.global.exception.InternalServiceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class DelegatingTokenManager implements TokenManager {
    private final List<TokenManager> providers;

    public DelegatingTokenManager(List<TokenManager> providers) {
        this.providers = providers;
    }

    @Override
    public Token generate(Token token) {
        Class<? extends Token> toTest = token.getClass();
        for (TokenManager provider : getProviders()) {
            if (!provider.supports(toTest)) {
                continue;
            }
            try {
                return provider.generate(token);
            } catch (AuthException exception) {
                throw exception;
            } catch (Exception exception) {
                log.error("critical error occurred while authenticating token");
                throw new InternalServiceException(InternalServiceErrorCode.UN_KNOWN, exception);
            }
        }
        throw new UnsupportedProviderException("TokenManager로 등록되지 않은 토큰의 인증을 시도했습니다.");
    }

    @Override
    public Token authenticate(Token token) {
        Class<? extends Token> toTest = token.getClass();
        Token result;
        for (TokenManager provider : getProviders()) {
            if (!provider.supports(toTest)) {
                continue;
            }
            try {
                result = provider.authenticate(token);
                if (result != null && result.isAuthenticated()) {
                    return result;
                }
            } catch (AuthException exception) {
                throw exception;
            } catch (Exception exception) {
                log.error("critical error occurred while authenticating token");
                throw new InternalServiceException(InternalServiceErrorCode.UN_KNOWN, exception);
            }
        }
        throw new UnsupportedProviderException("TokenManager로 등록되지 않은 토큰의 인증을 시도했습니다.");
    }

    @Override
    public void remove(Token token) {
        Class<? extends Token> toTest = token.getClass();
        for (TokenManager provider : getProviders()) {
            if (!provider.supports(toTest)) {
                continue;
            }
            try {
                provider.remove(token);
                return;
            } catch (AuthException exception) {
                throw exception;
            } catch (Exception exception) {
                log.error("critical error occurred while removing token");
                throw new InternalServiceException(InternalServiceErrorCode.UN_KNOWN, exception);
            }
        }
        throw new UnsupportedProviderException("TokenManager로 등록되지 않은 토큰의 삭제를 시도했습니다.");
    }

    @Override
    public boolean supports(Class<? extends Token> token) {
        return false;
    }

    private List<TokenManager> getProviders() {
        return providers;
    }
}
