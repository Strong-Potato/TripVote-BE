package fc.be.app.domain.member.provider;

import fc.be.app.domain.member.exception.AuthErrorCode;
import fc.be.app.domain.member.exception.AuthException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.Duration;
import java.util.Base64;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TokenProvider {
    private final RedisTemplate<String, String> redisTemplate;

    private static final int VERIFICATION_CODE_LENGTH = 6;
    private static final String VERIFICATION_CODE_PREFIX = "verification_code_";
    private static final Duration VERIFICATION_CODE_DURATION = Duration.ofSeconds(300);
    private static final String VERIFICATION_CODE_GENERATION_COUNT_PREFIX = "code_generation_count_";
    private static final Duration VERIFICATION_CODE_GENERATION_COUNT_DURATION = Duration.ofSeconds(300);
    private static final int VERIFICATION_CODE_GENERATION_BLOCKED_COUNT = 5;
    private static final String BLOCKED_EMAIL_PREFIX = "blocked_email_";
    private static final Duration BLOCKED_EMAIL_DURATION = Duration.ofSeconds(300);
    private static final String REGISTER_TOKEN_PREFIX = "register_token_";
    private static final Duration REGISTER_TOKEN_DURATION = Duration.ofMinutes(300);

    /**
     * generate and store code for later verification
     *
     * @param email email for verification
     * @return VerificationCode
     * @throws AuthException when target email is blocked due to frequent generate request
     */
    public String generateVerificationCode(String email) throws AuthException {
        if (isBlocked(email)) {
            String key = BLOCKED_EMAIL_PREFIX + email;
            Long expire = redisTemplate.getExpire(key);
            throw new AuthException(AuthErrorCode.VERIFICATION_CODE_GENERATE_BLOCKED, "data", Map.of("expire", expire));
        }
        countVerificationCodeGeneration(email);
        String key = VERIFICATION_CODE_PREFIX + email;
        String value = generateSecureRandomCode(VERIFICATION_CODE_LENGTH);
        redisTemplate.opsForValue().set(key, value, VERIFICATION_CODE_DURATION);
        return value;
    }

    private void countVerificationCodeGeneration(String email) {
        String key = VERIFICATION_CODE_GENERATION_COUNT_PREFIX + email;
        Long count = redisTemplate.opsForValue().increment(key);
        redisTemplate.expire(key, VERIFICATION_CODE_GENERATION_COUNT_DURATION);
        if (count != null && count >= VERIFICATION_CODE_GENERATION_BLOCKED_COUNT) {
            addToBlockedEmail(email);
        }
    }

    private void addToBlockedEmail(String email) {
        String key = BLOCKED_EMAIL_PREFIX + email;
        redisTemplate.opsForValue().set(key, "blacklisted");
        redisTemplate.expire(key, BLOCKED_EMAIL_DURATION);
    }

    public boolean isBlocked(String email) {
        String key = BLOCKED_EMAIL_PREFIX + email;
        return Boolean.TRUE.equals(redisTemplate.hasKey(key));
    }

    private String generateSecureRandomCode(int length) {
        SecureRandom secureRandom = new SecureRandom();
        byte[] randomBytes = new byte[length];
        secureRandom.nextBytes(randomBytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(randomBytes);
    }

    /**
     * authenticate verification code
     *
     * @param email            email for registration
     * @param verificationCode verificationCode sent to target email
     * @return true if VerificationCode is correct. else throws AuthException
     * @throws AuthException with CODE_EXPIRED when verificationCode is outdated
     * @throws AuthException when INCORRECT_CODE when verificationCode is incorrect
     */
    public boolean authenticateVerificationCode(String email, String verificationCode) throws AuthException {
        String key = VERIFICATION_CODE_PREFIX + email;
        String value = redisTemplate.opsForValue().get(key);
        if (value == null) {
            throw new AuthException(AuthErrorCode.CODE_EXPIRED);
        }
        if (!verificationCode.equals(value)) {
            throw new AuthException(AuthErrorCode.INCORRECT_CODE);
        }
        return true;
    }

    /**
     * generate and store register token for member registration
     *
     * @param email email for registration
     * @return RegisterToken
     */
    public String generateRegisterToken(String email) {
        String key = REGISTER_TOKEN_PREFIX + UUID.randomUUID();
        Boolean result;
        while ((result = redisTemplate.opsForValue().setIfAbsent(key, email, REGISTER_TOKEN_DURATION)) == null || result == false) {
            key = REGISTER_TOKEN_PREFIX + UUID.randomUUID();
        }
        return key.substring(REGISTER_TOKEN_PREFIX.length());
    }

    /**
     * authenticate register token
     *
     * @param email         email to be registered
     * @param registerToken token for member register
     * @return true if registerToken matches requested email
     * @throws AuthException with TOKEN_EXPIRED when registerToken is outdated
     * @throws AuthException when INCORRECT_CODE when registerToken is incorrect
     */
    public boolean authenticateRegisterToken(String email, String registerToken) throws AuthException {
        String key = REGISTER_TOKEN_PREFIX + registerToken;
        String verifiedEmail = redisTemplate.opsForValue().get(key);
        if (verifiedEmail == null) {
            throw new AuthException(AuthErrorCode.TOKEN_EXPIRED);
        }
        if (!verifiedEmail.equals(email)) {
            throw new AuthException(AuthErrorCode.INCORRECT_CODE);
        }
        return true;
    }

    /**
     * remove register token
     *
     * @param registerToken token used for member register
     */
    public void removeRegisterToken(String registerToken) {
        String key = REGISTER_TOKEN_PREFIX + registerToken;
        redisTemplate.opsForValue().getAndDelete(key);
    }


    /**
     * Set Set Type
     *
     * @param key
     * @param data
     */
    public void setValue(String key, String[] data) {
        SetOperations<String, String> operations = redisTemplate.opsForSet();
        for (String datum : data) {
            operations.add(key, datum);
        }
    }

    /**
     * Set Hash Type
     *
     * @param key
     * @param obj1 Hash Key
     * @param obj2 Hash Value
     */
    public void setValue(String key, Object obj1, Object obj2) {
        HashOperations<String, Object, Object> operations = redisTemplate.opsForHash();
        operations.put(key, obj1, obj2);
    }

    /**
     * Get Hash Type
     *
     * @param key
     * @param hash
     * @return
     */
    public Object getHashValue(String key, String hash) {
        HashOperations<String, Object, Object> operations = redisTemplate.opsForHash();
        return operations.get(key, hash);
    }

    /**
     * Delete Hash
     *
     * @param hashKey
     * @param key
     */
    public void deleteKey(String hashKey, String key) {
        redisTemplate.opsForHash().delete(hashKey, key);
    }
}
