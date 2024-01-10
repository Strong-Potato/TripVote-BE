package fc.be.app.global.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.annotation.web.configurers.FormLoginConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        // 기본 보안 설정
        httpSecurity
            .formLogin(FormLoginConfigurer::disable)
            .csrf(CsrfConfigurer::disable);

        httpSecurity
            .authorizeHttpRequests(authRequest -> authRequest
                .anyRequest().permitAll());

        return httpSecurity.build();
    }
}
