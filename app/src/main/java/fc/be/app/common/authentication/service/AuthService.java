package fc.be.app.common.authentication.service;


import fc.be.app.common.authentication.exception.AuthException;
import fc.be.app.domain.member.exception.MemberException;

public interface AuthService {
    /**
     * generate and store code for later verification
     *
     * @param email email for verification
     * @return VerificationCode
     * @throws AuthException when target email is blocked due to frequent generate request
     */
    String generateVerifyCode(String email) throws AuthException;

    void verifyEmail(String targetEmail, String verificationCode);

    void verifyPassword(Long id, String password) throws MemberException, AuthException;
}
