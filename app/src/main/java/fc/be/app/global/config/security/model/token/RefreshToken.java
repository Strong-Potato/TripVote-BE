package fc.be.app.global.config.security.model.token;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class RefreshToken implements Serializable {
    private String accessToken;
    private Long userId;
    private String clientIp;
    private String userAgent;
    private String value;
}
