package fc.be.app.global.config.security.service;

import fc.be.app.global.config.security.model.token.RefreshToken;
import fc.be.app.global.config.security.model.token.TokenPair;
import fc.be.app.global.config.security.model.user.UserPrincipal;
import fc.be.app.global.config.security.properties.TokenProperties;
import fc.be.app.global.config.security.provider.AuthenticationDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.Duration;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {
    private final RedisTemplate<String, RefreshToken> redisTemplate;
    private final TokenProperties tokenProperties;

    private static final String REFRESH_TOKEN_KEY_PREFIX = "refresh_token_";
    private static final Duration REFRESH_TOKEN_DURATION = Duration.ofHours(2);

    /**
     * check validation of access-token
     *
     * @param tokenPair     access token과 refresh token
     * @param userPrincipal login 당시의 user 정보 (현재 유저 X)
     * @param details       현재 접속한 client-ip와 user-agent
     * @return
     */
    public boolean isValid(TokenPair tokenPair, UserPrincipal userPrincipal, AuthenticationDetails details) {
        String accessToken = tokenPair.accessToken();
        String refreshToken = tokenPair.refreshToken();
        if (!StringUtils.hasText(accessToken) || !StringUtils.hasText(refreshToken)) {
            // no value
            return false;
        }
        Long id = userPrincipal.id();
        String clientIp = details.getClientIp();
        String userAgent = details.getUserAgent();

        RefreshToken requestedRefreshToken = new RefreshToken(accessToken, id, clientIp, userAgent, refreshToken);
        RefreshToken storedRefreshToken = getRefreshToken(accessToken);
        if (storedRefreshToken == null) {
            // expired
            return false;
        }
        if (!storedRefreshToken.equals(requestedRefreshToken)) {
            // invalid
            return false;
        }
        return true;
    }

    /**
     * generate refresh-token for requested access token
     *
     * @param accessToken
     * @param userPrincipal
     * @param details
     * @return generated refresh token value for the requested access token
     */
    public String refresh(String accessToken, UserPrincipal userPrincipal, AuthenticationDetails details) {
        String newRefreshTokenValue = UUID.randomUUID().toString();
        RefreshToken newRefreshToken =
                new RefreshToken(accessToken, userPrincipal.id(), details.getClientIp(), details.getUserAgent(), newRefreshTokenValue);
        saveRefreshToken(newRefreshToken);
        return newRefreshTokenValue;
    }

    private void saveRefreshToken(RefreshToken refreshToken) {
        String key = REFRESH_TOKEN_KEY_PREFIX + refreshToken.getAccessToken();
        redisTemplate.opsForValue().set(key, refreshToken, REFRESH_TOKEN_DURATION);
    }

    private RefreshToken getRefreshToken(String accessToken) {
        String key = REFRESH_TOKEN_KEY_PREFIX + accessToken;
        return redisTemplate.opsForValue().get(key);
    }

    private void deleteRefreshToken(String accessToken) {
        String key = REFRESH_TOKEN_KEY_PREFIX + accessToken;
        redisTemplate.delete(key);
    }
}
