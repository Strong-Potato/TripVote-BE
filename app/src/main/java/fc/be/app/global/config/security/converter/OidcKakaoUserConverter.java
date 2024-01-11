package fc.be.app.global.config.security.converter;

import fc.be.app.domain.member.vo.AuthProvider;
import fc.be.app.global.config.security.model.user.KakaoOidcUser;
import fc.be.app.global.config.security.model.user.ProviderUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;

public class OidcKakaoUserConverter implements Converter<ProviderUserConvertRequest, ProviderUser> {
    @Override
    public ProviderUser convert(ProviderUserConvertRequest request) {
        String registrationId = request.clientRegistration().getRegistrationId();
        if (registrationId.equals(AuthProvider.KAKAO.getRegistrationId())
                && request.oAuth2User() instanceof OidcUser oidcUser) {
            return new KakaoOidcUser(oidcUser, request.clientRegistration());
        }
        return null;
    }
}
