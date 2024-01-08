package fc.be.app.global.config.security.service;

import fc.be.app.domain.member.service.MemberCommand;
import fc.be.app.domain.member.service.MemberQuery;
import fc.be.app.global.config.security.converter.Converter;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;

@Service
public class CustomOidcUserService extends AbstractOAuth2UserService implements OAuth2UserService<OidcUserRequest, OidcUser> {
    private final OidcUserService defaultOidcUserService = new OidcUserService();

    public CustomOidcUserService(MemberQuery memberQuery, MemberCommand memberCommand, Converter converter) {
        super(memberQuery, memberCommand, converter);
    }

    @Override
    public OidcUser loadUser(OidcUserRequest userRequest) throws OAuth2AuthenticationException {
        ClientRegistration clientRegistration = userRequest.getClientRegistration();
        OidcUser oidcUser = defaultOidcUserService.loadUser(userRequest);
        // 회원가입
        super.register(clientRegistration, oidcUser, userRequest);
        return oidcUser;
    }
}
