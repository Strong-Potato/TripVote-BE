package fc.be.app.global.config.security.model;

import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;

public class KakaoOidcUser extends OAuth2ProviderUser {
    public KakaoOidcUser(OidcUser oidcUser, ClientRegistration clientRegistration) {
        super(oidcUser, clientRegistration);
    }

    @Override
    public String getId() {
        return String.valueOf(getFlattenedAttributes().get("sub"));
    }

    @Override
    public String getEmail() {
        return String.valueOf(getFlattenedAttributes().get("email"));
    }

    @Override
    public String getNickname() {
        return String.valueOf(getFlattenedAttributes().get("nickname"));
    }

    @Override
    public String getProfile() {
        return String.valueOf(getFlattenedAttributes().get("picture"));
    }
}
