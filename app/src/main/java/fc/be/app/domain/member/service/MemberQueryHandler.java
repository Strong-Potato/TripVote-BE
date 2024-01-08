package fc.be.app.domain.member.service;

import fc.be.app.domain.member.entity.Member;
import fc.be.app.domain.member.repository.MemberRepository;
import fc.be.app.domain.member.vo.AuthProvider;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
@AllArgsConstructor
public class MemberQueryHandler implements MemberQuery {
    private final MemberRepository memberRepository;

    @Override
    public Optional<MemberResponse> find(MemberRequest request) {
        Member findMember = memberRepository.findByProviderAndEmail(AuthProvider.NONE, request.email()).orElse(null);
        if (findMember == null) {
            return Optional.empty();
        }
        return Optional.of(
                new MemberResponse(
                        findMember.getId(),
                        findMember.getPassword(),
                        findMember.getEmail(),
                        findMember.getNickname(),
                        findMember.getProfile(),
                        findMember.getProvider()
                )
        );
    }

    @Override
    public Optional<MemberResponse> find(ProviderMemberRequest request) {
        Member findProviderMember = memberRepository.findByProviderAndEmail(request.provider(), request.email()).orElse(null);
        if (findProviderMember == null) {
            return Optional.empty();
        }
        return Optional.of(
                new MemberResponse(
                        findProviderMember.getId(),
                        findProviderMember.getPassword(),
                        findProviderMember.getEmail(),
                        findProviderMember.getNickname(),
                        findProviderMember.getProfile(),
                        findProviderMember.getProvider()
                )
        );
    }
}
