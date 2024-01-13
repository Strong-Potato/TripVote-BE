package fc.be.app.domain.member.service;

import fc.be.app.domain.member.entity.Member;
import fc.be.app.domain.member.repository.MemberRepository;
import fc.be.app.domain.member.vo.AuthProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberQueryHandler implements MemberQuery {
    private final MemberRepository memberRepository;

    @Override
    public Optional<MemberResponse> findById(Long id) {
        Optional<Member> findMember = memberRepository.findById(id);
        return findMember
                .map(member -> new MemberResponse(
                        member.getId(),
                        member.getPassword(),
                        member.getEmail(),
                        member.getNickname(),
                        member.getProfile(),
                        member.getProvider()));
    }

    @Override
    public Optional<MemberResponse> find(MemberRequest request) {
        Optional<Member> findMember = memberRepository.findByProviderAndEmail(AuthProvider.NONE, request.email());
        return findMember
                .map(member -> new MemberResponse(
                        member.getId(),
                        member.getPassword(),
                        member.getEmail(),
                        member.getNickname(),
                        member.getProfile(),
                        member.getProvider()));
    }

    @Override
    public Optional<MemberResponse> find(ProviderMemberRequest request) {
        Optional<Member> findProviderMember = memberRepository.findByProviderAndEmail(request.provider(), request.email());
        return findProviderMember
                .map(member -> new MemberResponse(
                        member.getId(),
                        member.getPassword(),
                        member.getEmail(),
                        member.getNickname(),
                        member.getProfile(),
                        member.getProvider()));
    }

    @Override
    public boolean exists(MemberRequest request) {
        return memberRepository.existsByProviderAndEmail(AuthProvider.NONE, request.email());
    }

    @Override
    public boolean exists(ProviderMemberRequest request) {
        return memberRepository.existsByProviderAndEmail(request.provider(), request.email());
    }
}
