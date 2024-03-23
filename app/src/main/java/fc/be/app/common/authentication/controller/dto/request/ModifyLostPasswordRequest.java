package fc.be.app.common.authentication.controller.dto.request;

import fc.be.app.domain.member.controller.validation.Password;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ModifyLostPasswordRequest(
        @NotBlank
        String token,
        @Email
        @NotNull
        String email,
        @Password
        String newPassword
) {
}
