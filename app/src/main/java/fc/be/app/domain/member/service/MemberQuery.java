package fc.be.app.domain.member.service;

import fc.be.app.domain.member.vo.AuthProvider;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

import java.util.Optional;

public interface MemberQuery {
    Optional<MemberResponse> findById(Long id);

    Optional<MemberResponse> find(@Valid MemberRequest request);

    Optional<MemberResponse> find(@Valid ProviderMemberRequest request);

    boolean exists(@Valid MemberRequest request);

    boolean exists(@Valid ProviderMemberRequest request);

    boolean verify(Long id, String password);

    boolean verify(String email, String password);

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
            AuthProvider provider,
            boolean isSubscribe
    ) {
    }
}
