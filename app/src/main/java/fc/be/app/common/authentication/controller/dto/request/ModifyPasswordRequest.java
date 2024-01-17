package fc.be.app.common.authentication.controller.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record ModifyPasswordRequest(
        @NotBlank
        String token,
        @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*[!@#$%^&*()])(?=.*[0-9]).{8,16}$")
        String newPassword
) {
}
