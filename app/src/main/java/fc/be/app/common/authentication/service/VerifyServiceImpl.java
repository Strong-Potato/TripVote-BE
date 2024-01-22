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
    private static final int GENERATION_LOCK_COUNT = 5;
    private static final Duration LOCK_DURATION = Duration.ofMinutes(30);
    private static final String COUNT_PREFIX = "count_";
    private static final String LOCKED_PREFIX = "blocked_email_";

    private final RedisClient redisClient;

    @Override
    public String lockableIssue(Purpose purpose, String target) throws AuthException {
        String prefix = CODE_PREFIX + purpose.name();
        if (isLocked(prefix, target)) {
            Long lockedExpire = getLockedExpire(prefix, target);
            throw new AuthException(AuthErrorCode.VERIFICATION_CODE_GENERATE_BLOCKED, "data", Map.of("expire", lockedExpire));
        }
        lockIfExceed(prefix, target, GENERATION_LOCK_COUNT, GENERATION_COUNT_DURATION, LOCK_DURATION);
        String randomKey = generateSecureRandomCode(purpose.getLengthBefEncoding());
        redisClient.setValue(prefix, target, randomKey, purpose.getCodeDuration());
        return randomKey;
    }

    @Override
    public void verify(Purpose purpose, String target, String code) {
        String value = redisClient.getValue(CODE_PREFIX + purpose.name(), target);
        if (value == null) {
            throw new AuthException(AuthErrorCode.CODE_EXPIRED);
        }
        if (!value.equals(code)) {
            throw new AuthException(AuthErrorCode.INCORRECT_CODE);
        }
    }

    @Override
    public void setCodeInfo(Purpose purpose, String code, Map<String, String> codeInfo) {
        String prefix = CODE_INFO_PREFIX + purpose.name();
        redisClient.setValues(prefix, code, codeInfo, purpose.getCodeDuration());
    }

    @Override
    public Map<String, String> getCodeInfo(Purpose purpose, String code) {
        String prefix = CODE_INFO_PREFIX + purpose.name();
        return redisClient.getValues(prefix, code);
    }

    private boolean isLocked(String prefix, String target) {
        return redisClient.isExist(LOCKED_PREFIX + prefix, target);
    }

    private void lockIfExceed(String prefix, String target, int maxCount, Duration countDuration, Duration lockDuration) {
        Long count = redisClient.increment(COUNT_PREFIX + prefix, target);
        redisClient.setExpire(COUNT_PREFIX + prefix, target, countDuration);
        if (count != null && count >= maxCount) {
            lock(prefix, target, lockDuration);
        }
    }

    private void lock(String prefix, String target, Duration lockDuration) {
        redisClient.setValue(LOCKED_PREFIX + prefix, target, "blacklisted", lockDuration);
    }

    private Long getLockedExpire(String prefix, String target) {
        return redisClient.getExpire(LOCKED_PREFIX + prefix, target);
    }

    private String generateSecureRandomCode(int length) {
        SecureRandom secureRandom = new SecureRandom();
        byte[] randomBytes = new byte[length];
        secureRandom.nextBytes(randomBytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(randomBytes);
    }
}
