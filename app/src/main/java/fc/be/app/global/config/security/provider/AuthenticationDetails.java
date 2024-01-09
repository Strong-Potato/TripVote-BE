package fc.be.app.global.config.security.provider;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.web.authentication.WebAuthenticationDetails;

import java.util.Objects;

public class AuthenticationDetails extends WebAuthenticationDetails {
    private final String clientIp;
    private final String userAgent;

    public AuthenticationDetails(HttpServletRequest request) {
        super(request);
        this.clientIp = extractClientIp(request);
        this.userAgent = request.getHeader("User-Agent");
    }

    public String getClientIp() {
        return clientIp;
    }

    public String getUserAgent() {
        return userAgent;
    }

    private static String extractClientIp(HttpServletRequest request) {
        String[] headerNames =
                {"X-Forwarded-For", "Proxy-Client-IP", "WL-Proxy-Client-IP", "HTTP_CLIENT_IP", "HTTP_X_FORWARDED_FOR"};
        for (String header : headerNames) {
            String ip = request.getHeader(header);
            if (ip != null && ip.length() != 0 && !"unknown".equalsIgnoreCase(ip)) {
                return ip;
            }
        }
        return request.getRemoteAddr();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AuthenticationDetails)) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        AuthenticationDetails that = (AuthenticationDetails) o;
        return Objects.equals(clientIp, that.clientIp) && Objects.equals(userAgent, that.userAgent);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), clientIp, userAgent);
    }

    @Override
    public String toString() {
        return String.format("%s [RemoteIpAddress=%s, SessionId=%s, clientIp=%s, userAgent=%s]",
                getClass().getSimpleName(), this.getRemoteAddress(), this.getSessionId(), this.getClientIp(), this.getUserAgent());
    }
}
