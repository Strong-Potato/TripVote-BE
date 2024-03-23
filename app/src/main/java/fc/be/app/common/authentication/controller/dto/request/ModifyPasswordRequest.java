package fc.be.app.common.authentication.controller.dto.request;

import fc.be.app.domain.member.controller.validation.Password;
import jakarta.validation.constraints.NotBlank;

public record ModifyPasswordRequest(
        @NotBlank
        String token,
        @Password
        String newPassword
) {
}
