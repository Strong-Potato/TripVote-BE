package fc.be.app.common.authentication.manager;

import fc.be.app.common.authentication.model.ModifyToken;
import fc.be.app.common.authentication.model.RandomKeyToken;
import fc.be.app.common.authentication.model.Token;
import fc.be.app.common.authentication.repository.RedisClient;
import fc.be.app.global.exception.InternalServiceErrorCode;
import fc.be.app.global.exception.InternalServiceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.UUID;

@Component
@Slf4j
public class ModifyTokenManager extends AbstractRandomKeyTokenManager {
    public ModifyTokenManager(RedisClient redisClient) {
        super(redisClient);
    }

    @Override
    protected RandomKeyToken createSuccessToken(RandomKeyToken randomKeyToken) {
        ModifyToken authenticatedToken = (ModifyToken) randomKeyToken;
        return ModifyToken.authenticated(authenticatedToken.getTokenValue(), authenticatedToken.getTarget(), authenticatedToken.getAuthenticationStrategy());
    }

    @Override
    public Token generate(Token tokenToGenerate) {
        ModifyToken modifyTokenToGenerate = (ModifyToken) tokenToGenerate;
        String prefix = modifyTokenToGenerate.getPrefix();
        String target;
        switch (modifyTokenToGenerate.getAuthenticationStrategy()) {
            case ID -> target = String.valueOf(modifyTokenToGenerate.getTarget());
            case EMAIL -> target = (String) modifyTokenToGenerate.getTarget();
            case null, default -> {
                log.error("authentication strategy must be set");
                throw new InternalServiceException(InternalServiceErrorCode.UN_KNOWN);
            }
        }
        Duration duration = modifyTokenToGenerate.getDuration();
        String tokenValue = UUID.randomUUID().toString();
        Boolean result;
        while ((result = redisClient.setValueIfAbsent(prefix, tokenValue, target, duration)) == null || result == false) {
            tokenValue = UUID.randomUUID().toString();
        }
        return ModifyToken.unauthenticated(tokenValue, target, modifyTokenToGenerate.getAuthenticationStrategy());
    }

    @Override
    public boolean supports(Class<? extends Token> token) {
        return ModifyToken.class.isAssignableFrom(token);
    }
}
