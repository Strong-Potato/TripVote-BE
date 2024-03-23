package fc.be.app.common.authentication.controller.dto.request;

import fc.be.app.domain.member.controller.validation.Password;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record RegisterMemberRequest(
        @NotBlank
        String token,
        @Email
        @NotNull
        String email,
        @Password
        String password,
        @NotBlank
        String nickname,
        @Pattern(regexp = "^https?://\\S+$")
        String profile
) {
}
