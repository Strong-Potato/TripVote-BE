package fc.be.app.global.config;

import fc.be.app.global.interceptor.SpaceInterceptor;
import fc.be.app.global.util.SpaceTypeRequestConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@RequiredArgsConstructor
@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final SpaceInterceptor spaceInterceptor;

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new SpaceTypeRequestConverter());
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(spaceInterceptor)
                .addPathPatterns("/spaces/{spaceId}", "/spaces/recent");
    }

}
