package fc.be.app.domain.member.service;

import com.auth0.jwt.interfaces.DecodedJWT;
import fc.be.app.common.authentication.exception.AuthErrorCode;
import fc.be.app.common.authentication.exception.AuthException;
import fc.be.app.domain.member.entity.Member;
import fc.be.app.domain.member.exception.MemberErrorCode;
import fc.be.app.domain.member.exception.MemberException;
import fc.be.app.domain.member.repository.MemberRepository;
import fc.be.app.domain.member.vo.AuthProvider;
import fc.be.app.domain.space.repository.JoinedMemberRepository;
import fc.be.app.global.util.JwtService;
import fc.be.app.domain.space.service.SpaceService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberCommandHandler implements MemberCommand {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final JoinedMemberRepository joinedMemberRepository;
    private final SpaceService spaceService;

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
        Member savedMember = memberRepository.save(newMember);
        spaceService.createSpace(savedMember.getId());
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
        Member savedProviderMember = memberRepository.save(newProviderMember);
        spaceService.createSpace(savedProviderMember.getId());
    }

    @Override
    public void modifyPassword(Long id, String newPassword) {
        Member targetMember = memberRepository.findById(id)
                .orElseThrow(() -> new MemberException(MemberErrorCode.MEMBER_NOT_FOUND));
        if (passwordEncoder.matches(newPassword, targetMember.getPassword())) {
            throw new AuthException(AuthErrorCode.SAME_PASSWORD);
        }
        targetMember.changePassword(passwordEncoder.encode(newPassword));
    }

    @Override
    public void modifyPassword(String email, String newPassword) {
        Member targetMember = memberRepository.findByProviderAndEmail(AuthProvider.NONE, email)
                .orElseThrow(() -> new MemberException(MemberErrorCode.MEMBER_NOT_FOUND));
        if (passwordEncoder.matches(newPassword, targetMember.getPassword())) {
            throw new AuthException(AuthErrorCode.SAME_PASSWORD);
        }
        targetMember.changePassword(passwordEncoder.encode(newPassword));
    }

    @Override
    public void modifyUserInfo(Long id, String newNickname, String newProfile) {
        Member targetMember = memberRepository.findById(id)
                .orElseThrow(() -> new MemberException(MemberErrorCode.MEMBER_NOT_FOUND));
        targetMember.changeInfo(newNickname, newProfile);
    }

    @Override
    public void deactivate(MemberDeactivateRequest request) {
        Long id = request.id();
        String password = request.password();
        Member targetMember = memberRepository.findById(id)
                .orElseThrow(() -> new MemberException(MemberErrorCode.MEMBER_NOT_FOUND));
        if (!passwordEncoder.matches(password, targetMember.getPassword())) {
            throw new AuthException(AuthErrorCode.INCORRECT_PASSWORD);
        }
        targetMember.deactivate();
        joinedMemberRepository.updateAllByMemberId(id, false);
    }

    @Override
    public void deactivate(ProviderMemberDeactivateRequest request) {
        Long id = request.id();
        String token = request.token();
        DecodedJWT verifiedToken = jwtService.verify(token);
        Instant minutesBef = LocalDateTime.now().atZone(ZoneId.systemDefault()).minusMinutes(10).toInstant();
        if (verifiedToken.getIssuedAt().before(Date.from(minutesBef))) {
            throw new AuthException(AuthErrorCode.LOGIN_REQUIRED);
        }
        Member targetMember = memberRepository.findById(id)
                .orElseThrow(() -> new MemberException(MemberErrorCode.MEMBER_NOT_FOUND));
        targetMember.deactivate();
        joinedMemberRepository.updateAllByMemberId(id, false);
    }
}
