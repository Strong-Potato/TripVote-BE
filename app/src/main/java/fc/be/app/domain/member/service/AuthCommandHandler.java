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

@Service
@Transactional
@RequiredArgsConstructor
public class AuthCommandHandler implements AuthCommand {
    private final TokenProvider tokenProvider;
    private final MemberQuery memberQuery;
    private final PasswordEncoder passwordEncoder;

    @Override
    public String generateAndStoreCode(String targetEmail) {
        return tokenProvider.generateVerificationCode(targetEmail);
    }

    @Override
    public String generateAndStoreToken(String email, String verificationCode) {
        tokenProvider.authenticateVerificationCode(email, verificationCode);
        return tokenProvider.generateRegisterToken(email);
    }

    @Override
    public void authenticate(String email, String registerToken) {
        tokenProvider.authenticateRegisterToken(email, registerToken);
    }

    @Override
    public void removeRegisterToken(String registerToken) {
        tokenProvider.removeRegisterToken(registerToken);
    }

    @Override
    public String generateAndStoreModifyToken(Long id, String password) {
        MemberQuery.MemberResponse memberResponse =
                memberQuery.findById(id).orElseThrow(() -> new MemberException(MemberErrorCode.MEMBER_NOT_FOUND));
        boolean matches = passwordEncoder.matches(password, memberResponse.password());
        if (!matches) {
            throw new AuthException(AuthErrorCode.INCORRECT_PASSWORD);
        }
        String modifyToken = tokenProvider.generateModifyToken(id);
        return modifyToken;
    }

    @Override
    public void authenticateModifyToken(Long id, String modifyToken) {
        tokenProvider.authenticateModifyToken(id, modifyToken);
    }

    @Override
    public void removeModifyToken(Long id) {
        tokenProvider.removeModifyToken(id);
    }
}
