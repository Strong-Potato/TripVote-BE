package fc.be.app.domain.member.service;


import fc.be.app.domain.member.exception.AuthException;
import fc.be.app.domain.member.exception.MemberException;

public interface AuthCommand {
    /**
     * @param email
     * @return
     * @throws AuthException
     */
    String generateAndStoreCode(String email) throws AuthException;

    /**
     * @param email
     * @param verificationCode
     * @return
     * @throws AuthException
     */
    String generateAndStoreToken(String email, String verificationCode) throws AuthException;

    /**
     * @param email
     * @param token
     * @throws AuthException
     */
    void authenticate(String email, String token) throws AuthException;

    /**
     * @param token
     */
    void removeRegisterToken(String token);

    /**
     * @param id
     * @param password
     * @return
     * @throws MemberException
     * @throws AuthException
     */
    String generateAndStoreModifyToken(Long id, String password) throws MemberException, AuthException;

    /**
     * @param id
     * @param modifyToken
     * @throws AuthException
     */
    void authenticateModifyToken(Long id, String modifyToken) throws AuthException;

    /**
     * @param id
     */
    void removeModifyToken(Long id);
}
