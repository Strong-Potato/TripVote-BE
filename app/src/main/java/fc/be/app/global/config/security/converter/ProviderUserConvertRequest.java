package fc.be.app.global.config.security.converter;

import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.core.user.OAuth2User;

public record ProviderUserConvertRequest(
        ClientRegistration clientRegistration,
        OAuth2User oAuth2User
) {
}