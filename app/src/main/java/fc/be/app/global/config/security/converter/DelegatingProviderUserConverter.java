package fc.be.app.global.config.security.converter;

import fc.be.app.global.config.security.exception.UnsupportedProviderException;
import fc.be.app.global.config.security.model.KakaoOidcUser;
import fc.be.app.global.config.security.model.KakaoUser;
import fc.be.app.global.config.security.model.OAuth2ProviderUser;
import fc.be.app.global.config.security.model.ProviderUser;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * <h3>Provider마다 응답형식이 매우 다양하므로 이를 통합된 형식으로 관리하기 위한 converter</h3>
 * <p>
 * OAuth2UserService(또는 OidcUserService)에서 반환하는 OAuth2User(또는 OidcUser)는 Provider에 따라 attributes의 값이 상이합니다.
 * 따라서 OAuth2User(또는 OidcUser) 객체들을 (자체 회원가입과 같은) 이후의 인증에서 공통되게 처리하기 위해서는 이들을 공통된 형식으로 묶어주어야 합니다.
 * DelegatingProviderUserConverter는 Provider 및 인증 종류에 따라 반환되는 다양한 형식의 User 객체들을 {@link ProviderUser}라는 통합된 형식으로 변환시켜 반환합니다.
 * </p>
 *
 * @author donghar
 * @see ProviderUser
 * @see OAuth2ProviderUser
 * @see KakaoUser
 * @see KakaoOidcUser
 */
@Component
public class DelegatingProviderUserConverter implements Converter<ProviderUserConvertRequest, ProviderUser> {
    private final List<Converter<ProviderUserConvertRequest, ProviderUser>> converters;

    public DelegatingProviderUserConverter() {
        List<Converter<ProviderUserConvertRequest, ProviderUser>> converters =
                Arrays.asList(
                        new OAuth2KakaoUserConverter(),
                        new OidcKakaoUserConverter()
                );
        this.converters = Collections.unmodifiableList(new LinkedList<>(converters));
    }

    @Override
    public ProviderUser convert(ProviderUserConvertRequest request) {
        for (Converter<ProviderUserConvertRequest, ProviderUser> converter : converters) {
            ProviderUser providerUser = converter.convert(request);
            if (providerUser != null) {
                return providerUser;
            }
        }
        throw new UnsupportedProviderException("지원하지 않는 Provider 입니다.");
    }
}
