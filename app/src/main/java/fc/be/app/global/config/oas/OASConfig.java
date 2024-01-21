package fc.be.app.global.config.oas;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Configuration;

@SecurityScheme(
        type = SecuritySchemeType.APIKEY, in = SecuritySchemeIn.COOKIE,
        name = "access-token", description = "JWT AccessToken"
)
@Configuration
public class OASConfig {
}
