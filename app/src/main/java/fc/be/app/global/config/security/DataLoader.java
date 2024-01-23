package fc.be.app.global.config.security;

import fc.be.app.domain.member.entity.Member;
import fc.be.app.domain.member.repository.MemberRepository;
import fc.be.app.domain.member.vo.AuthProvider;
import fc.be.app.domain.member.vo.MemberStatus;
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
        String email = "test@test.com";
        boolean isExists = memberRepository.existsByProviderAndEmail(AuthProvider.NONE, email);
        if (!isExists) {
            Member member = Member.builder()
                    .email(email)
                    .nickname("ok")
                    .password(passwordEncoder.encode("1q2w3e4r!Q"))
                    .profile("http://k.kakaocdn.net/dn/1G9kp/btsAot8liOn/8CWudi3uy07rvFNUkk3ER0/img_640x640.jpg")
                    .provider(AuthProvider.NONE)
                    .status(MemberStatus.ACTIVATED)
                    .build();

            memberRepository.save(member);
        }
    }
}