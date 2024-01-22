package fc.be.app.common.authentication.manager;

import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.exceptions.AlgorithmMismatchException;
import com.auth0.jwt.exceptions.InvalidClaimException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import fc.be.app.common.authentication.exception.AuthErrorCode;
import fc.be.app.common.authentication.exception.AuthException;
import fc.be.app.common.authentication.model.JwtToken;
import fc.be.app.common.authentication.model.Token;
import fc.be.app.global.exception.InternalServiceErrorCode;
import fc.be.app.global.exception.InternalServiceException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class AbstractJwtTokenManager implements TokenManager {
    @Override
    public Token authenticate(Token token) {
        JwtToken jwtToken = (JwtToken) token;
        String tokenValue = determineTokenValue(jwtToken);
        JWTVerifier jwtVerifier = determineVerifier(jwtToken);
        DecodedJWT verified;
        try {
            verified = jwtVerifier.verify(tokenValue);
        } catch (TokenExpiredException exception) {
            throw new AuthException(AuthErrorCode.TOKEN_EXPIRED);
        } catch (InvalidClaimException exception) {
            throw new AuthException(AuthErrorCode.INCORRECT_TOKEN);
        } catch (AlgorithmMismatchException | SignatureVerificationException exception) {
            log.warn("Suspicious activity detected. {}", exception.getMessage(), exception);
            throw new AuthException(AuthErrorCode.INCORRECT_TOKEN);
        }
        JwtToken authenticatedToken = createSuccessToken(jwtToken, verified);
        if (!authenticatedToken.isAuthenticated()) {
            log.error("isAuthenticated method of success token must return true");
            throw new InternalServiceException(InternalServiceErrorCode.UN_KNOWN);
        }
        return authenticatedToken;
    }

    protected String determineTokenValue(JwtToken token) {
        if (token.getTokenValue() != null) {
            return token.getTokenValue();
        }
        return "NONE_PROVIDED";
    }

    abstract protected JWTVerifier determineVerifier(JwtToken token);

    abstract protected JwtToken createSuccessToken(JwtToken token, DecodedJWT verified);

    @Override
    public void remove(Token token) {
    }
}
