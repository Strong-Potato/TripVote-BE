package fc.be.app.global.config.security.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties("jwt")
@Getter
@Setter
public class TokenProperties {
    private String secretKey;
    private String accessTokenName;
    private String refreshTokenName;
    private String accessTokenExpireTime;
    private String accessTokenCookieExpireTime;
    private String refreshTokenExpireTime;
    private String refreshTokenCookieExpireTime;
    private String issuer;
    private String accessTokenAuthenticationProperty;
    private String accessTokenAuthoritiesProperty;
}
