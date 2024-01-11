package fc.be.app.domain.member.service;


import fc.be.app.domain.member.exception.AuthException;

public interface AuthCommand {
    String generateAndStoreCode(String email) throws AuthException;

    String generateAndStoreToken(String email, String verificationCode) throws AuthException;

    void authenticate(String email, String token) throws AuthException;

    void removeRegisterToken(String token);
}
