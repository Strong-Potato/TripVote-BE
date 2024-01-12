package fc.be.app.global.config.security.model.token;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Objects;

@Getter
@NoArgsConstructor
public class RefreshToken implements Serializable {
    private static final long serialVersionUID = 1L;

    private String accessToken;
    private Long userId;
    private String clientIp;
    private String userAgent;
    private String value;

    public RefreshToken(String accessToken, Long userId, String clientIp, String userAgent, String value) {
        this.accessToken = accessToken;
        this.userId = userId;
        this.clientIp = clientIp;
        this.userAgent = userAgent;
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RefreshToken that = (RefreshToken) o;
        return Objects.equals(accessToken, that.accessToken) && Objects.equals(userId, that.userId) && Objects.equals(clientIp, that.clientIp) && Objects.equals(userAgent, that.userAgent) && Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(accessToken, userId, clientIp, userAgent, value);
    }
}
