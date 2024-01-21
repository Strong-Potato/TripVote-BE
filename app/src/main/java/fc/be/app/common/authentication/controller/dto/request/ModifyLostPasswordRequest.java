package fc.be.app.common.authentication.controller.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record ModifyLostPasswordRequest(
        @NotBlank
        String token,
        @Email
        @NotNull
        String email,
        @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*[!@#$%^&*()])(?=.*[0-9]).{8,16}$")
        String newPassword
) {
}
