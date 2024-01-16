package fc.be.app.domain.member.service;

import fc.be.app.domain.member.entity.Member;
import fc.be.app.domain.member.exception.MemberErrorCode;
import fc.be.app.domain.member.exception.MemberException;
import fc.be.app.domain.member.repository.MemberRepository;
import fc.be.app.domain.member.vo.AuthProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberCommandHandler implements MemberCommand {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void register(MemberRegisterRequest request) {
        boolean isExists = memberRepository.existsByProviderAndEmail(AuthProvider.NONE, request.email());
        if (isExists) {
            return;
        }
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
        boolean isExists = memberRepository.existsByProviderAndEmail(request.provider(), request.email());
        if (isExists) {
            return;
        }
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

    @Override
    public void modifyPassword(Long id, String newPassword) {
        Member targetMember =
                memberRepository.findById(id).orElseThrow(() -> new MemberException(MemberErrorCode.MEMBER_NOT_FOUND));
        targetMember.changePassword(passwordEncoder.encode(newPassword));
    }

    @Override
    public void modifyPassword(String email, String newPassword) {
        Member targetMember =
                memberRepository.findByProviderAndEmail(AuthProvider.NONE, email).orElseThrow(() -> new MemberException(MemberErrorCode.MEMBER_NOT_FOUND));
        targetMember.changePassword(passwordEncoder.encode(newPassword));
    }

    @Override
    public void modifyUserInfo(Long id, String newNickname, String newProfile) {
        Member targetMember =
                memberRepository.findById(id).orElseThrow(() -> new MemberException(MemberErrorCode.MEMBER_NOT_FOUND));
        targetMember.changeInfo(newNickname, newProfile);
    }
}
