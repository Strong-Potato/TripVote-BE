package fc.be.app.domain.member.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AuthProvider {
    KAKAO("kakao"), NONE("none");

    private final String registrationId;

    public static AuthProvider of(String registrationId) {
        for (AuthProvider value : AuthProvider.values()) {
            if (value.getRegistrationId().equals(registrationId))
                return value;
        }
        throw new IllegalArgumentException("존재하지 않는 Provider 입니다.");
    }
}
