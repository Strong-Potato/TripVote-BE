package fc.be.app.global.config.security.service;

import fc.be.app.domain.member.service.MemberCommand;
import fc.be.app.domain.member.service.MemberCommand.ProviderMemberRegisterRequest;
import fc.be.app.domain.member.vo.AuthProvider;
import fc.be.app.global.config.security.converter.Converter;
import fc.be.app.global.config.security.converter.ProviderUserConvertRequest;
import fc.be.app.global.config.security.model.user.ProviderUser;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;

public abstract class AbstractOAuth2UserService {
    private final MemberCommand memberCommand;
    private final Converter<ProviderUserConvertRequest, ProviderUser> converter;

    public AbstractOAuth2UserService(MemberCommand memberCommand, Converter converter) {
        this.memberCommand = memberCommand;
        this.converter = converter;
    }

    protected void register(ClientRegistration clientRegistration, OAuth2User oAuth2User, OAuth2UserRequest userRequest)
            throws InternalAuthenticationServiceException {
        ProviderUserConvertRequest convertRequest = new ProviderUserConvertRequest(clientRegistration, oAuth2User);
        ProviderUser providerUser = converter.convert(convertRequest);
        try {
            registerAsOurs(providerUser, userRequest);
        } catch (Exception exception) {
            throw new InternalAuthenticationServiceException("OAuth2인증에 성공하였으나, 우리 회원으로 등록하지 못했습니다.", exception);
        }
    }

    private void registerAsOurs(ProviderUser providerUser, OAuth2UserRequest userRequest) {
        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        ProviderMemberRegisterRequest registerRequest =
                new ProviderMemberRegisterRequest(providerUser.getEmail(), providerUser.getNickname(), providerUser.getProfile(), AuthProvider.of(registrationId), providerUser.getId());
        memberCommand.register(registerRequest);
    }
}
