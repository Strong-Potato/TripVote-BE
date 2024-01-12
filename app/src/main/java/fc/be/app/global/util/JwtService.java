package fc.be.app.global.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import fc.be.app.global.config.security.model.user.UserPrincipal;
import fc.be.app.global.config.security.properties.TokenProperties;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Service
public class JwtService {
    private final Algorithm algorithm;
    private final String issuer;
    private final JWTVerifier verifier;
    private final TokenProperties tokenProperties;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public JwtService(TokenProperties tokenProperties) {
        this.algorithm = Algorithm.HMAC256(tokenProperties.getSecretKey());
        this.issuer = tokenProperties.getIssuer();
        this.verifier = JWT.require(algorithm).withIssuer(issuer).build();
        this.tokenProperties = tokenProperties;
    }

    public String generateAccessToken(UserPrincipal userPrincipal) {
        String subject = userPrincipal.email();
        Map<String, Map<String, ?>> claim = new HashMap<>();
        Map<String, Object> authenticationMap = objectMapper.convertValue(userPrincipal,
                new TypeReference<Map<String, Object>>() {
                });
        claim.put(tokenProperties.getAccessTokenAuthenticationProperty(), authenticationMap);
        Map<String, String[]> arrayClaim = new HashMap<>();
        SimpleGrantedAuthority userAuthority = new SimpleGrantedAuthority("USER");
        String[] _authorities = new String[]{userAuthority.getAuthority()};
        arrayClaim.put(tokenProperties.getAccessTokenAuthoritiesProperty(), _authorities);
        return generateToken(subject, claim, arrayClaim, Long.parseLong(tokenProperties.getAccessTokenExpireTime()));
    }

    public String generateAccessToken(UserPrincipal userPrincipal, Collection<? extends GrantedAuthority> authorities) {
        String subject = userPrincipal.email();
        Map<String, Map<String, ?>> claim = new HashMap<>();
        Map<String, Object> authenticationMap = objectMapper.convertValue(userPrincipal,
                new TypeReference<Map<String, Object>>() {
                });
        claim.put(tokenProperties.getAccessTokenAuthenticationProperty(), authenticationMap);
        Map<String, String[]> arrayClaim = new HashMap<>();
        String[] _authorities = authorities.stream().map(GrantedAuthority::getAuthority).toArray(String[]::new);
        arrayClaim.put(tokenProperties.getAccessTokenAuthoritiesProperty(), _authorities);
        return generateToken(subject, claim, arrayClaim, Long.parseLong(tokenProperties.getAccessTokenExpireTime()));
    }

    private String generateToken(String subject, Map<String, Map<String, ?>> claim, Map<String, String[]> arrayClaim, long expireSecond) {
        LocalDateTime now = LocalDateTime.now();
        JWTCreator.Builder builder = JWT.create();
        builder.withSubject(subject);
        for (Map.Entry<String, Map<String, ?>> claimEntry : claim.entrySet()) {
            builder.withClaim(claimEntry.getKey(), claimEntry.getValue());
        }
        for (Map.Entry<String, String[]> arrayClaimEntry : arrayClaim.entrySet()) {
            builder.withArrayClaim(arrayClaimEntry.getKey(), arrayClaimEntry.getValue());
        }
        builder
                .withIssuer(issuer)
                .withIssuedAt(now.atZone(ZoneId.systemDefault()).toInstant())
                .withExpiresAt(now.atZone(ZoneId.systemDefault()).plus(expireSecond, ChronoUnit.SECONDS).toInstant());
        return builder.sign(algorithm);
    }

    public DecodedJWT verify(String token) throws JWTVerificationException {
        return verifier.verify(token);
    }
}
