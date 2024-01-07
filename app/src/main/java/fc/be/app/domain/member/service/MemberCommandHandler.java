package fc.be.app.domain.member.service;

import fc.be.app.domain.member.entity.Member;
import fc.be.app.domain.member.repository.MemberRepository;
import fc.be.app.domain.member.vo.AuthProvider;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@AllArgsConstructor
public class MemberCommandHandler implements MemberCommand {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void register(MemberRegisterRequest request) {
        Member newMember = Member.builder()
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .nickname(request.nickname())
                .profile(request.profile() != null ? request.profile() : "https://i.ibb.co/zJ8XrB0/img-user.png")
                .provider(AuthProvider.NONE)
                .providedId("none")
                .build();
        memberRepository.save(newMember);
    }

    @Override
    public void register(ProviderMemberRegisterRequest request) {
        Member newProviderMember = Member.builder()
                .email(request.email())
                .password(passwordEncoder.encode("none"))
                .nickname(request.nickname())
                .profile(request.profile())
                .provider(request.provider())
                .providedId(request.providedId())
                .build();
        memberRepository.save(newProviderMember);
    }
}
