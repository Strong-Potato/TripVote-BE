package fc.be.app.global.config.security.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.convert.DurationUnit;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Configuration
@ConfigurationProperties("cors")
@Getter
@Setter
public class CorsProperties {

    private List<String> allowedOrigins = new ArrayList<>();

    private List<String> allowedOriginPatterns = new ArrayList<>();

    private List<String> allowedMethods = new ArrayList<>();

    private List<String> allowedHeaders = new ArrayList<>();

    private List<String> exposedHeaders = new ArrayList<>();

    private Boolean allowCredentials;

    @DurationUnit(ChronoUnit.SECONDS)
    private Duration maxAge = Duration.ofSeconds(1800);
}