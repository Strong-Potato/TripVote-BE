package fc.be.app.common.authentication.service;

import fc.be.app.common.authentication.exception.AuthErrorCode;
import fc.be.app.common.authentication.exception.AuthException;
import fc.be.app.common.authentication.repository.RedisClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.Duration;
import java.util.Base64;
import java.util.Map;

@Service
@Transactional
@RequiredArgsConstructor
public class VerifyServiceImpl implements VerifyService {
    private static final String CODE_PREFIX = "verification_code_";
    private static final String CODE_INFO_PREFIX = "verification_code_info";
    private static final Duration GENERATION_COUNT_DURATION = Duration.ofSeconds(300);
    private static final int GENERATION_BLOCK_COUNT = 5;
    private static final Duration BLOCK_DURATION = Duration.ofSeconds(300);

    private final RedisClient redisClient;

    @Override
    public String lockableIssue(Purpose purpose, String target) throws AuthException {
        lockable(purpose, target);
        String randomKey = generateSecureRandomCode(purpose.getLengthBefEncoding());
        redisClient.setValue(CODE_PREFIX, purpose.name() + target, randomKey, purpose.getCodeDuration());
        return randomKey;
    }

    private void lockable(Purpose purpose, String target) {
        if (redisClient.isBlocked(CODE_PREFIX, purpose.name() + target)) {
            Long blockedExpire = redisClient.getBlockedExpire(CODE_PREFIX, purpose.name() + target);
            throw new AuthException(AuthErrorCode.VERIFICATION_CODE_GENERATE_BLOCKED, "data", Map.of("expire", blockedExpire));
        }
        // count++ and block if exceed
        redisClient.blockIfExceed(CODE_PREFIX, purpose.name() + target, GENERATION_BLOCK_COUNT, GENERATION_COUNT_DURATION, BLOCK_DURATION);
    }

    @Override
    public void verify(Purpose purpose, String target, String code) {
        String value = redisClient.getStringValue(CODE_PREFIX, purpose.name() + target);
        if (value == null) {
            throw new AuthException(AuthErrorCode.CODE_EXPIRED);
        }
        if (!value.equals(code)) {
            throw new AuthException(AuthErrorCode.INCORRECT_CODE);
        }
    }

    @Override
    public void setCodeInfo(Purpose purpose, String code, Map<String, String> codeInfo) {
        redisClient.setValue(CODE_INFO_PREFIX, purpose.name() + code, codeInfo, purpose.getCodeDuration());
    }

    @Override
    public Map<String, Object> getCodeInfo(Purpose purpose, String code) {
        return redisClient.getHashValues(CODE_INFO_PREFIX, purpose.name() + code);
    }

    private String generateSecureRandomCode(int length) {
        SecureRandom secureRandom = new SecureRandom();
        byte[] randomBytes = new byte[length];
        secureRandom.nextBytes(randomBytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(randomBytes);
    }
}
