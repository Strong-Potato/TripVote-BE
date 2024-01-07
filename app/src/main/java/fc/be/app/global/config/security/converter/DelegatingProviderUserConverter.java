package fc.be.app.global.config.security.converter;

import fc.be.app.global.config.security.model.ProviderUser;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * <h3>Provider 마다 응답형식이 매우 다양하므로 이를 관리하기 편하게 공통된 형식으로 통합하기 위한 converter</h3>
 * <p>
 * OAuth2UserService(또는 OidcUserService)에서 반환하는 OAuth2User(또는 OidcUser)는 Provider에 따라 attributes의 값이 상이합니다.
 * 따라서 이 OAuth2User(또는 OidcUser) 객체들을 이후 인증에서 공통되게 처리하기 위해서는 이들을 공통된 양식으로 묶어주어야 합니다.
 * DelegatingProviderUserConverter는 각각의 Provider 및 인증 종류에 따라 반환되는 다양한 형식의 User 객체들을 ProviderUser의 형식으로 변환시켜 반환합니다.
 * </p>
 *
 * @author donghar
 * @see ProviderUser
 * @see fc.be.app.global.config.security.model.OAuth2ProviderUser
 * @see fc.be.app.global.config.security.model.KakaoUser
 * @see fc.be.app.global.config.security.model.KakaoOidcUser
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

    @Nullable
    @Override
    public ProviderUser convert(ProviderUserConvertRequest request) {
        Assert.notNull(request, "providerUserRequest cannot be null");
        for (Converter<ProviderUserConvertRequest, ProviderUser> converter : converters) {
            ProviderUser providerUser = converter.convert(request);
            if (providerUser != null) {
                return providerUser;
            }
        }
        return null;
    }
}
