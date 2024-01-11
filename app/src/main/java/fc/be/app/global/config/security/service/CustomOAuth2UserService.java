package fc.be.app.global.config.security.service;

import fc.be.app.domain.member.service.MemberCommand;
import fc.be.app.global.config.security.converter.Converter;
import fc.be.app.global.config.security.model.user.ProviderUser;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

/**
 * <h2>{@link DefaultOAuth2UserService}를 사용했을 때 문제점</h2>
 * <h3>OAuth2User(auth by Google)</h3>
 * <p>Name: [113050102916187260572],</p>
 * <p>Granted Authorities: [[OAUTH2_USER, SCOPE_https://www.googleapis.com/auth/userinfo.email, SCOPE_https://www.googleapis.com/auth/userinfo.profile, SCOPE_openid]],</p>
 * <p>User Attributes: [{sub=113050102916187260572, name=DH K, given_name=DH, family_name=K, picture=https://lh3.googleusercontent.com/a/ACg8ocIUjZdug-c-fyv3X5xSmFeq-Kp1eU5Eupar5p7PMkKx=s96-c, email=donghar2004@gmail.com, email_verified=true, locale=ko}]</p>
 * <br>
 *
 * <h3>OAuth2User(auth by Naver)</h3>
 * <p>Name: [{id=d4WMKD5aU4SSu4K8upcndCFQsajbPU_yi_lcBr15nHs, profile_image=https://ssl.pstatic.net/static/pwe/address/img_profile.png, email=donghar@naver.com, name=고동훤}],</p>
 * <p>Granted Authorities: [[OAUTH2_USER]],</p>
 * <p>User Attributes: [{resultcode=00, message=success, response={id=d4WMKD5aU4SSu4K8upcndCFQsajbPU_yi_lcBr15nHs, profile_image=https://ssl.pstatic.net/static/pwe/address/img_profile.png, email=donghar@naver.com, name=고동훤}}]</p>
 * <br>
 *
 * <h3>1. Google의 SCOPE가 'url' 로 되어있다.</h3>
 * <p>GrantedAuthoritiesMapper를 customize해서 'url'을 제거한다.</p>
 * <br>
 *
 * <h3>2. Naver의 사용자 정보가 Granted Authorities로 바인딩이 안된다.</h3>
 * <p>Scope와 Authority는 엄밀히 말하면 서로 다른 의미를 가지고 있습니다.</p>
 * <ul>
 *     <li>
 *         Scope: OAuth 클라이언트가 리소스에 접근하기 위해 요청할 수 있는 권한 범위. ex)"profile", "email", "contacts"
 *     </li>
 *     <li>
 *         Authority: 인증 주체의 권한. ex)"ROLE_USER", "ROLE_ADMIN"
 *     </li>
 * </ul>
 * <ul>
 *     <p>Before: oAuth2User.getAuthorities() = [ROLE_USER, SCOPE_email, SCOPE_profile]</p>
 *     <p>After: oAuth2User.getAuthorities() = [OAUTH2_USER]</p>
 * </ul>
 * <p>네이버의 경우(Google/Kakao는 필요없음) OAuth 인증 유저의 SCOPE 범위에 따라 인가 처리가 필요하면 OAuth2User의 SCOPE에 따라 별도의 ROLE로 매핑해주는 GrantedAuthoritiesMapper를 만들고,</p>
 * <p>1. {@link org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer}의 hasAuthority()를 이용</p>
 * <p>2. @PreAuthorize 어노테이션을 이용</p>
 * <p>의 방법으로 처리해야 합니다.</p>
 * <p><a href="https://docs.spring.io/spring-security/reference/servlet/oauth2/login/advanced.html#oauth2login-advanced-map-authorities">참고</a></p>
 * <br>
 *
 * <h3>3. Naver의 Attributes정보가 response로 wrapping 되어있다.</h3>
 * <p>
 * Provider가 제공하는 OAuth2User가 상이하므로 우리 서비스에서 사용할 수 있는 형태의 User 객체로 변화시켜 사용한다.
 * 이 때 적절한 추상화를 해서 Provider들의 응답 변화에 변경이 용이하고, 서비스 내에서 OAuth2User 객체를 관리하기 쉽게 만든다.
 * </p>
 * <br>
 *
 * @author donghar
 * @see ProviderUser
 * @see org.springframework.security.oauth2.core.user.OAuth2UserAuthority
 */
@Service
public class CustomOAuth2UserService extends AbstractOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {
    private final OAuth2UserService<OAuth2UserRequest, OAuth2User> defaultOAuth2UserService = new DefaultOAuth2UserService();

    public CustomOAuth2UserService(MemberCommand memberCommand, Converter converter) {
        super(memberCommand, converter);
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        ClientRegistration clientRegistration = userRequest.getClientRegistration();
        OAuth2User oAuth2User = defaultOAuth2UserService.loadUser(userRequest);
        // 회원가입
        super.register(clientRegistration, oAuth2User, userRequest);
        return oAuth2User;
    }
}
