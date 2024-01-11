package fc.be.app.global.config.security;

import fc.be.app.domain.member.entity.Member;
import fc.be.app.domain.member.repository.MemberRepository;
import fc.be.app.domain.member.vo.AuthProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        String email = "ok@gmail.com";
        boolean isExists = memberRepository.existsByProviderAndEmail(AuthProvider.NONE, email);
        if (!isExists) {
            Member member = Member.builder()
                    .email("ok@gmail.com")
                    .nickname("ok")
                    .password(passwordEncoder.encode("1234"))
                    .provider(AuthProvider.NONE)
                    .build();

            memberRepository.save(member);
        }
    }
}