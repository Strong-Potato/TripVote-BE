package fc.be.openapi.algolia;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties("algolia")
public class AlgoliaProperties {
    private String applicationId;
    private String apiKey;
    private String indexName;
}
