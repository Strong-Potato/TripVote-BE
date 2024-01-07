package fc.be.app.domain.member.service;

import fc.be.app.domain.member.vo.AuthProvider;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.Optional;

public interface MemberQuery {
    Optional<MemberResponse> find(MemberRequest request);

    Optional<MemberResponse> find(ProviderMemberRequest request);

    record MemberRequest(
            @Min(value = 1)
            Long id
    ) {
    }

    record ProviderMemberRequest(
            @NotNull
            AuthProvider provider,
            @NotBlank
            String providedId
    ) {
    }

    record MemberResponse(
            Long id,
            String email,
            String nickname,
            String profile,
            AuthProvider provider
    ) {
    }
}
