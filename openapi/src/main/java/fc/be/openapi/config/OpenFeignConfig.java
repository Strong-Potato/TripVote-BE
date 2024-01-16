package fc.be.openapi.config;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Profile("prod")
@Configuration
@EnableFeignClients(basePackages = "fc.be.openapi")
public class OpenFeignConfig {
}
