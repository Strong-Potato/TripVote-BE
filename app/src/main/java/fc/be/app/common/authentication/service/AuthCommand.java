package fc.be.app.common.authentication.service;


import fc.be.app.common.authentication.exception.AuthException;
import fc.be.app.domain.member.exception.MemberException;

public interface AuthCommand {
    /**
     * generate and store code for later verification
     *
     * @param email email for verification
     * @return VerificationCode
     * @throws AuthException when target email is blocked due to frequent generate request
     */
    String generateVerifyCode(String email) throws AuthException;

    /**
     * authenticate verification code
     * then generate and store register token for member registration
     *
     * @param email            email for registration
     * @param verificationCode verificationCode sent to target email
     * @return RegisterToken
     * @throws AuthException with CODE_EXPIRED when verificationCode is outdated
     * @throws AuthException when INCORRECT_CODE when verificationCode is incorrect
     */
    String generateRegisterToken(String email, String verificationCode) throws AuthException;

    /**
     * authenticate register token
     *
     * @param email         email to be registered
     * @param registerToken token for member register
     * @throws AuthException with TOKEN_EXPIRED when registerToken is outdated
     * @throws AuthException when INCORRECT_TOKEN when registerToken is incorrect
     */
    void authenticateRegisterToken(String email, String registerToken) throws AuthException;

    /**
     * remove token used for member register
     *
     * @param registerToken token used for member register
     */
    void removeRegisterToken(String registerToken);

    /**
     * @param id       target user id for modify password
     * @param password target user password
     * @return modify token
     * @throws MemberException if target user doesn't exist
     * @throws AuthException   if password is incorrect
     */
    String generateModifyToken(Long id, String password) throws MemberException, AuthException;

    /**
     * @param email            email of which password to be modified
     * @param verificationCode verificationCode sent to target email
     * @return modify token
     */
    String generateModifyToken(String email, String verificationCode);

    /**
     * @param id          target user id for modify password
     * @param modifyToken token for modify password
     * @throws AuthException with TOKEN_EXPIRED when modify-token is outdated
     * @throws AuthException when INCORRECT_TOKEN when modify-token is incorrect
     */
    void authenticateModifyToken(Long id, String modifyToken) throws AuthException;

    /**
     * @param email       target user email for modify password
     * @param modifyToken token for modify password
     * @throws AuthException with TOKEN_EXPIRED when modify-token is outdated
     * @throws AuthException when INCORRECT_TOKEN when modify-token is incorrect
     */
    void authenticateModifyToken(String email, String modifyToken) throws AuthException;

    /**
     * remove modify token
     *
     * @param id modified user id
     */
    void removeModifyToken(Long id);

    /**
     * remove modify token
     *
     * @param email modified user email
     */
    void removeModifyToken(String email);
}
