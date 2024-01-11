package fc.be.app.global.config.security.model.user;

import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.core.user.OAuth2User;


public class KakaoUser extends OAuth2ProviderUser {
    private static final String ACCOUNT_PREFIX = "kakao_account_";
    private static final String PROFILE_PREFIX = ACCOUNT_PREFIX + "profile_";

    public KakaoUser(OAuth2User oAuth2User, ClientRegistration clientRegistration) {
        super(oAuth2User, clientRegistration);
    }

    @Override
    public String getId() {
        return String.valueOf(getFlattenedAttributes().get("id"));
    }

    @Override
    public String getEmail() {
        return String.valueOf(getFlattenedAttributes().get(ACCOUNT_PREFIX + "email"));
    }

    @Override
    public String getNickname() {
        return String.valueOf(getFlattenedAttributes().get(PROFILE_PREFIX + "nickname"));
    }

    @Override
    public String getProfile() {
        return String.valueOf(getFlattenedAttributes().get(PROFILE_PREFIX + "profile_image_url"));
    }
}
