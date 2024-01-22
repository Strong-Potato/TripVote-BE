package fc.be.app.common.authentication.manager;

import fc.be.app.common.authentication.exception.AuthErrorCode;
import fc.be.app.common.authentication.exception.AuthException;
import fc.be.app.common.authentication.model.RandomKeyToken;
import fc.be.app.common.authentication.model.Token;
import fc.be.app.common.authentication.repository.RedisClient;
import fc.be.app.global.exception.InternalServiceErrorCode;
import fc.be.app.global.exception.InternalServiceException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class AbstractRandomKeyTokenManager implements TokenManager {
    protected final RedisClient redisClient;

    protected AbstractRandomKeyTokenManager(RedisClient redisClient) {
        this.redisClient = redisClient;
    }

    @Override
    public Token authenticate(Token token) {
        RandomKeyToken randomKeyToken = (RandomKeyToken) token;
        Object target = retrieveTarget(randomKeyToken);

        authenticationChecks(randomKeyToken, target);

        RandomKeyToken successToken = createSuccessToken(randomKeyToken);
        if (!successToken.isAuthenticated()) {
            log.error("isAuthenticated method of success token must return true");
            throw new InternalServiceException(InternalServiceErrorCode.UN_KNOWN);
        }
        return successToken;
    }

    protected Object retrieveTarget(RandomKeyToken token) {
        return token.getTarget();
    }

    protected void authenticationChecks(RandomKeyToken token, Object target) throws AuthException {
        String value = redisClient.getValue(token.getPrefix(), token.getTokenValue());
        if (value == null) {
            throw new AuthException(AuthErrorCode.TOKEN_EXPIRED);
        }
        if (!value.equals(target)) {
            throw new AuthException(AuthErrorCode.INCORRECT_TOKEN);
        }
    }

    protected abstract RandomKeyToken createSuccessToken(RandomKeyToken randomKeyToken);

    @Override
    public void remove(Token token) {
        RandomKeyToken randomKeyToken = (RandomKeyToken) token;
        redisClient.remove(randomKeyToken.getPrefix(), randomKeyToken.getTokenValue());
    }
}
