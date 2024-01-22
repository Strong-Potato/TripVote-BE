package fc.be.app.common.authentication.manager;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import fc.be.app.common.authentication.model.JoinSpaceToken;
import fc.be.app.common.authentication.model.JwtToken;
import fc.be.app.common.authentication.model.Token;
import fc.be.app.global.config.security.properties.TokenProperties;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Component
public class JoinSpaceTokenManager extends AbstractJwtTokenManager {
    private static final String PURPOSE_KEY = "purpose";
    private static final String PURPOSE_VALUE = "join-space";
    private static final String TARGET_SPACE_ID_KEY = "space-id";
    private static final Duration EXPIRE_DURATION = Duration.ofHours(2);

    private final Algorithm algorithm;

    public JoinSpaceTokenManager(TokenProperties tokenProperties) {
        this.algorithm = Algorithm.HMAC256(tokenProperties.getSecretKey());
    }

    @Override
    public Token generate(Token tokenToGenerate) {
        JoinSpaceToken joinSpaceTokenToGenerate = (JoinSpaceToken) tokenToGenerate;
        String issuer = joinSpaceTokenToGenerate.getIssuer();
        Long targetSpaceId = joinSpaceTokenToGenerate.getTargetSpaceId();
        LocalDateTime now = LocalDateTime.now();
        String jwtTokenValue = JWT.create()
                .withIssuer(issuer)
                .withClaim(PURPOSE_KEY, PURPOSE_VALUE)
                .withClaim(TARGET_SPACE_ID_KEY, targetSpaceId)
                .withIssuedAt(now.atZone(ZoneId.systemDefault()).toInstant())
                .withExpiresAt(now.atZone(ZoneId.systemDefault()).plus(EXPIRE_DURATION).toInstant())
                .sign(algorithm);
        return JoinSpaceToken.unauthenticated(jwtTokenValue, issuer, targetSpaceId);
    }

    @Override
    public boolean supports(Class<? extends Token> token) {
        return JoinSpaceToken.class.isAssignableFrom(token);
    }

    @Override
    protected JWTVerifier determineVerifier(JwtToken token) {
        JoinSpaceToken joinSpaceToken = (JoinSpaceToken) token;
        String expectedIssuer = joinSpaceToken.getIssuer();
        Long expectedTargetSpaceId = joinSpaceToken.getTargetSpaceId();
        return generateVerifier(expectedIssuer, expectedTargetSpaceId);
    }

    @Override
    protected JwtToken createSuccessToken(JwtToken token, DecodedJWT verified) {
        return JoinSpaceToken.authenticated(verified.getIssuer(), verified.getClaim(TARGET_SPACE_ID_KEY).asLong());
    }

    private JWTVerifier generateVerifier(String issuer, Long targetSpaceId) {
        return JWT.require(algorithm)
//                .withIssuer(issuer)
                .withClaim(PURPOSE_KEY, PURPOSE_VALUE)
                .withClaim(TARGET_SPACE_ID_KEY, targetSpaceId)
                .build();
    }
}
