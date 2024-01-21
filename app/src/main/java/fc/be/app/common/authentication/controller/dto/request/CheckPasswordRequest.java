package fc.be.app.common.authentication.controller.dto.request;

import jakarta.validation.constraints.NotBlank;

public record CheckPasswordRequest(
        @NotBlank
        String password
) {
}
