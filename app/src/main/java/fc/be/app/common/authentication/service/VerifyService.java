package fc.be.app.common.authentication.service;


import fc.be.app.common.authentication.exception.AuthException;

import java.time.Duration;
import java.util.Map;

public interface VerifyService {
    /**
     * generate and store code for later verification
     *
     * @param purpose purpose and verification strategy of this code
     * @param target  target to verify
     * @return VerificationCode
     * @throws AuthException when target is blocked due to frequent generate request
     */
    String lockableIssue(Purpose purpose, String target) throws AuthException;

    /**
     * @param purpose purpose and verification strategy of this code
     * @param target  target to verify
     * @param code    verification-code sent for authorized client
     * @throws AuthException when code is expired or incorrect
     */
    void verify(Purpose purpose, String target, String code) throws AuthException;

    void setCodeInfo(Purpose purpose, String code, Map<String, String> codeInfo);

    Map<String, Object> getCodeInfo(Purpose purpose, String code);

    enum Purpose {
        EMAIL(6, Duration.ofSeconds(300)), JOIN_SPACE(6, Duration.ofDays(7));

        private final int lengthBefEncoding;
        private final Duration codeDuration;

        Purpose(int lengthBefEncoding, Duration codeDuration) {
            this.lengthBefEncoding = lengthBefEncoding;
            this.codeDuration = codeDuration;
        }

        public int getLengthBefEncoding() {
            return lengthBefEncoding;
        }

        public Duration getCodeDuration() {
            return codeDuration;
        }
    }
}
