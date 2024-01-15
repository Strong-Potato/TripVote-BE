package fc.be.app.domain.member.dto.request;

import jakarta.validation.constraints.NotBlank;

public record CheckPasswordRequest(
        @NotBlank
        String password
) {
}
