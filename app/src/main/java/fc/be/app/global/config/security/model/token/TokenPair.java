package fc.be.app.global.config.security.model.token;

public record TokenPair(
        String accessToken,
        String refreshToken,
        boolean isRegenerated
) {
}
