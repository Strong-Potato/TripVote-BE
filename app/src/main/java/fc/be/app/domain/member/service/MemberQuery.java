package fc.be.app.domain.member.service;

import fc.be.app.domain.member.vo.AuthProvider;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

import java.util.Optional;

public interface MemberQuery {
    Optional<MemberResponse> find(MemberRequest request);

    Optional<MemberResponse> find(ProviderMemberRequest request);

    record MemberRequest(
            @Email
            String email
    ) {
    }

    record ProviderMemberRequest(
            @NotNull
            AuthProvider provider,
            @Email
            String email
    ) {
    }

    record MemberResponse(
            Long id,
            String password,
            String email,
            String nickname,
            String profile,
            AuthProvider provider
    ) {
    }
}
