package fc.be.notification.config;


import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties("fcm")
@Getter
@Setter
public class FcmProperties {
    private String tokenType;
    private String requestUrlPrefix;
    private String requestUrlPostfix;
    private String keyPath;
    private String googleAuthUrl;
    private String projectId;
}
