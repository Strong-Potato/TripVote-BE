package fc.be.app.domain.member.service;

import fc.be.app.domain.member.exception.AuthErrorCode;
import fc.be.app.domain.member.exception.AuthException;
import fc.be.app.domain.member.provider.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthCommandHandler implements AuthCommand {
    private final TokenProvider tokenProvider;

    @Override
    public String generateAndStoreCode(String targetEmail) throws AuthException {
        return tokenProvider.generateVerificationCode(targetEmail);
    }

    @Override
    public String generateAndStoreToken(String email, String verificationCode) throws AuthException {
        tokenProvider.authenticateVerificationCode(email, verificationCode);
        return tokenProvider.generateRegisterToken(email);
    }

    @Override
    public void authenticate(String email, String registerToken) throws AuthException {
        boolean isAuthenticated = tokenProvider.authenticateRegisterToken(email, registerToken);
        if (!isAuthenticated) {
            throw new AuthException(AuthErrorCode.INCORRECT_TOKEN);
        }
    }

    @Override
    public void removeRegisterToken(String registerToken) {
        tokenProvider.removeRegisterToken(registerToken);
    }
}
