package fc.be.app.common.authentication.service;

import fc.be.app.common.authentication.exception.AuthErrorCode;
import fc.be.app.common.authentication.exception.AuthException;
import fc.be.app.common.authentication.repository.RedisClient;
import fc.be.app.domain.member.exception.MemberErrorCode;
import fc.be.app.domain.member.exception.MemberException;
import fc.be.app.domain.member.service.MemberQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.Duration;
import java.util.Base64;
import java.util.Map;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private static final String VERIFICATION_CODE_PREFIX = "verification_code_";
    private static final int VERIFICATION_CODE_GENERATION_BLOCKED_COUNT = 5;
    private static final Duration VERIFICATION_CODE_GENERATION_COUNT_DURATION = Duration.ofSeconds(300);
    private static final Duration BLOCKED_EMAIL_DURATION = Duration.ofSeconds(300);
    private static final Duration VERIFICATION_CODE_DURATION = Duration.ofSeconds(300);

    private final RedisClient redisClient;
    private final MemberQuery memberQuery;
    private final PasswordEncoder passwordEncoder;

    @Override
    public String generateVerifyCode(String targetEmail) throws AuthException {
        if (redisClient.isBlocked(VERIFICATION_CODE_PREFIX, targetEmail)) {
            Long blockedExpire = redisClient.getBlockedExpire(VERIFICATION_CODE_PREFIX, targetEmail);
            throw new AuthException(AuthErrorCode.VERIFICATION_CODE_GENERATE_BLOCKED, "data", Map.of("expire", blockedExpire));
        }
        // count++ and block if exceed
        redisClient.blockIfExceed(VERIFICATION_CODE_PREFIX, targetEmail, VERIFICATION_CODE_GENERATION_BLOCKED_COUNT, VERIFICATION_CODE_GENERATION_COUNT_DURATION, BLOCKED_EMAIL_DURATION);
        String randomKey = generateSecureRandomCode(6);
        redisClient.setValue(VERIFICATION_CODE_PREFIX, targetEmail, randomKey, VERIFICATION_CODE_DURATION);
        return randomKey;
    }

    @Override
    public void verifyEmail(String targetEmail, String verificationCode) {
        String value = redisClient.getStringValue(VERIFICATION_CODE_PREFIX, targetEmail);
        if (value == null) {
            throw new AuthException(AuthErrorCode.CODE_EXPIRED);
        }
        if (!value.equals(verificationCode)) {
            throw new AuthException(AuthErrorCode.INCORRECT_CODE);
        }
    }

    @Override
    public void verifyPassword(Long id, String password) {
        MemberQuery.MemberResponse memberResponse =
                memberQuery.findById(id).orElseThrow(() -> new MemberException(MemberErrorCode.MEMBER_NOT_FOUND));
        boolean matches = passwordEncoder.matches(password, memberResponse.password());
        if (!matches) {
            throw new AuthException(AuthErrorCode.INCORRECT_PASSWORD);
        }
    }

    private String generateSecureRandomCode(int length) {
        SecureRandom secureRandom = new SecureRandom();
        byte[] randomBytes = new byte[length];
        secureRandom.nextBytes(randomBytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(randomBytes);
    }
}
