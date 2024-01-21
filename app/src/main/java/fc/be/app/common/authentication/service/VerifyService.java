package fc.be.app.common.authentication.service;


import fc.be.app.common.authentication.exception.AuthException;

import java.time.Duration;

public interface VerifyService {
    /**
     * generate and store code for later verification
     *
     * @param target target for verification
     * @return VerificationCode
     * @throws AuthException when target is blocked due to frequent generate request
     */
    String issueCode(Purpose purpose, String target) throws AuthException;

    /**
     * @param purpose
     * @param target
     * @param code
     * @throws AuthException when code is expired or incorrect
     */
    void verify(Purpose purpose, String target, String code) throws AuthException;

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
