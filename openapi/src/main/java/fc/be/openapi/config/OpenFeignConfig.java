package fc.be.openapi.config;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients(basePackages = "fc.be.openapi")
public class OpenFeignConfig {
}
