package fc.be.app.global.config.security.model;

import org.springframework.security.core.GrantedAuthority;

import java.util.List;

public interface ProviderUser {
    String getId();

    String getEmail();

    String getNickname();

    String getProfile();

    String getProvider();

    List<? extends GrantedAuthority> getAuthorities();
}
