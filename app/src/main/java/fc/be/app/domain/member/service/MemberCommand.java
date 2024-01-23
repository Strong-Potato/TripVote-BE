package fc.be.app.domain.member.service;

import fc.be.app.domain.member.vo.AuthProvider;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

public interface MemberCommand {
    void register(@Valid MemberRegisterRequest request);

    void register(@Valid ProviderMemberRegisterRequest request);

    void modifyPassword(Long id, String newPassword);

    void modifyPassword(String email, String newPassword);

    void modifyUserInfo(Long id, String newNickname, String newProfile);

    void deactivate(MemberDeactivateRequest request);

    void deactivate(ProviderMemberDeactivateRequest request);

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

    record MemberDeactivateRequest(
            @Positive
            Long id,
            @NotBlank
            String password
    ) {
    }

    record ProviderMemberDeactivateRequest(
            @Positive
            Long id,
            String token
    ) {
    }
}
