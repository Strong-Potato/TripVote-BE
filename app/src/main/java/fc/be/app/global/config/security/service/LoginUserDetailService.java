package fc.be.app.global.config.security.service;

import fc.be.app.domain.member.service.MemberQuery;
import fc.be.app.domain.member.service.MemberQuery.MemberRequest;
import fc.be.app.domain.member.service.MemberQuery.MemberResponse;
import fc.be.app.global.config.security.model.LoginUser;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class LoginUserDetailService implements UserDetailsService {
    private final MemberQuery memberQuery;

    public LoginUserDetailService(MemberQuery memberQuery) {
        this.memberQuery = memberQuery;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        MemberRequest request = new MemberRequest(username);
        MemberResponse response = memberQuery.find(request)
                .orElseThrow(() -> new UsernameNotFoundException("no such user"));
        List<GrantedAuthority> roles = new ArrayList<>();
        roles.add(new SimpleGrantedAuthority("USER"));
        return new LoginUser(response.id(), response.email(), response.password(), roles);
    }
}
