package fc.be.app.common.authentication.manager;

import fc.be.app.common.authentication.model.RandomKeyToken;
import fc.be.app.common.authentication.model.RegisterToken;
import fc.be.app.common.authentication.model.Token;
import fc.be.app.common.authentication.repository.RedisClient;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.UUID;

@Component
public class RegisterTokenManager extends AbstractRandomKeyTokenManager {
    protected RegisterTokenManager(RedisClient redisClient) {
        super(redisClient);
    }

    @Override
    protected RandomKeyToken createSuccessToken(RandomKeyToken randomKeyToken) {
        RegisterToken authenticatedToken = (RegisterToken) randomKeyToken;
        return RegisterToken.authenticated(authenticatedToken.getTokenValue(), (String) authenticatedToken.getTarget());
    }

    @Override
    public Token generate(Token tokenToGenerate) {
        RegisterToken registerTokenToGenerate = (RegisterToken) tokenToGenerate;
        String prefix = registerTokenToGenerate.getPrefix();
        String targetEmail = (String) registerTokenToGenerate.getTarget();
        Duration duration = registerTokenToGenerate.getDuration();
        String tokenValue = UUID.randomUUID().toString();
        Boolean result;
        while ((result = redisClient.setValueIfAbsent(prefix, tokenValue, targetEmail, duration)) == null || result == false) {
            tokenValue = UUID.randomUUID().toString();
        }
        return RegisterToken.unauthenticated(tokenValue, targetEmail);
    }

    @Override
    public boolean supports(Class<? extends Token> token) {
        return RegisterToken.class.isAssignableFrom(token);
    }
}
