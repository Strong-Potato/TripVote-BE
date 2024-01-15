package fc.be.app.domain.member.service;

import fc.be.app.domain.member.exception.AuthErrorCode;
import fc.be.app.domain.member.exception.AuthException;
import fc.be.app.domain.member.exception.MemberErrorCode;
import fc.be.app.domain.member.exception.MemberException;
import fc.be.app.domain.member.provider.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.UUID;

import static fc.be.app.domain.member.provider.properties.TokenProperties.*;


@Service
@Transactional
@RequiredArgsConstructor
public class AuthCommandHandler implements AuthCommand {
    private final TokenProvider tokenProvider;
    private final MemberQuery memberQuery;
    private final PasswordEncoder passwordEncoder;

    @Override
    public String generateVerifyCode(String targetEmail) {
        return tokenProvider.generateWithFrequentCheck(
                VERIFICATION_CODE_PREFIX,
                targetEmail,
                () -> generateSecureRandomCode(6),
                VERIFICATION_CODE_DURATION,
                VERIFICATION_CODE_GENERATION_BLOCKED_COUNT,
                VERIFICATION_CODE_GENERATION_COUNT_DURATION,
                BLOCKED_EMAIL_DURATION
        );
    }

    @Override
    public String generateRegisterToken(String email, String verificationCode) {
        tokenProvider.authenticate(VERIFICATION_CODE_PREFIX, email, verificationCode);
        return tokenProvider.generateWithRandomKey(
                REGISTER_TOKEN_PREFIX,
                email,
                () -> UUID.randomUUID().toString(),
                REGISTER_TOKEN_DURATION);
    }

    @Override
    public void authenticateRegisterToken(String email, String registerToken) {
        tokenProvider.authenticate(REGISTER_TOKEN_PREFIX, registerToken, email);
    }

    @Override
    public void removeRegisterToken(String registerToken) {
        tokenProvider.removeToken(REGISTER_TOKEN_PREFIX, registerToken);
    }

    @Override
    public String generateModifyToken(Long id, String password) {
        MemberQuery.MemberResponse memberResponse =
                memberQuery.findById(id).orElseThrow(() -> new MemberException(MemberErrorCode.MEMBER_NOT_FOUND));
        boolean matches = passwordEncoder.matches(password, memberResponse.password());
        if (!matches) {
            throw new AuthException(AuthErrorCode.INCORRECT_PASSWORD);
        }
        String modifyToken = tokenProvider.generateWithRandomKey(
                MODIFY_TOKEN_PREFIX,
                String.valueOf(id),
                () -> UUID.randomUUID().toString(),
                MODIFY_TOKEN_DURATION);

        return modifyToken;
    }

    @Override
    public void authenticateModifyToken(Long id, String modifyToken) {
        tokenProvider.authenticate(MODIFY_TOKEN_PREFIX, modifyToken, String.valueOf(id));
    }

    @Override
    public void removeModifyToken(Long id) {
        tokenProvider.removeToken(MODIFY_TOKEN_PREFIX, String.valueOf(id));
    }

    private String generateSecureRandomCode(int length) {
        SecureRandom secureRandom = new SecureRandom();
        byte[] randomBytes = new byte[length];
        secureRandom.nextBytes(randomBytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(randomBytes);
    }
}
