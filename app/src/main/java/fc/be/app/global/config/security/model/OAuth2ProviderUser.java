package fc.be.app.global.config.security.model;

import fc.be.app.global.util.FlattenMapUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.List;
import java.util.Map;

public abstract class OAuth2ProviderUser implements ProviderUser {
    private Map<String, Object> flattenedAttributes;
    private OAuth2User oAuth2User;
    private ClientRegistration clientRegistration;

    public OAuth2ProviderUser(OAuth2User oAuth2User, ClientRegistration clientRegistration) {
        this.flattenedAttributes = FlattenMapUtils.flattenMap(oAuth2User.getAttributes());
        this.oAuth2User = oAuth2User;
        this.clientRegistration = clientRegistration;
    }

    @Override
    public String getProvider() {
        return clientRegistration.getRegistrationId();
    }

    @Override
    public List<? extends GrantedAuthority> getAuthorities() {
        return oAuth2User.getAuthorities().stream().toList();
    }

    protected Map<String, Object> getFlattenedAttributes() {
        return flattenedAttributes;
    }
}
