package fc.be.app.common.authentication.provider.properties;

import java.time.Duration;

public class TokenProperties {
    public static final int VERIFICATION_CODE_GENERATION_BLOCKED_COUNT = 5;
    public static final String VERIFICATION_CODE_PREFIX = "verification_code_";
    public static final Duration VERIFICATION_CODE_GENERATION_COUNT_DURATION = Duration.ofSeconds(300);
    public static final Duration VERIFICATION_CODE_DURATION = Duration.ofSeconds(300);
    public static final Duration BLOCKED_EMAIL_DURATION = Duration.ofSeconds(300);
    public static final Duration REGISTER_TOKEN_DURATION = Duration.ofMinutes(300);
    public static final String MODIFY_TOKEN_PREFIX = "modify_token_";
    public static final Duration MODIFY_TOKEN_DURATION = Duration.ofMinutes(300);
    public static final String REGISTER_TOKEN_PREFIX = "register_token_";
}
