package fc.be.app.domain.member.service;

import fc.be.app.domain.member.vo.AuthProvider;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public interface MemberCommand {
    void register(@Valid MemberRegisterRequest request);

    void register(@Valid ProviderMemberRegisterRequest request);

    void modifyPassword(Long id, String newPassword);

    record MemberRegisterRequest(
            @Email
            String email,
            @Size(min = 6)
            String password,
            @NotBlank
            String nickname,
            String profile
    ) {
    }

    record ProviderMemberRegisterRequest(
            @Email
            String email,
            @NotBlank
            String nickname,
            @NotBlank
            String profile,
            @NotNull
            AuthProvider provider,
            @NotBlank
            String providedId
    ) {
    }
}
