package fc.be.app.domain.member.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CheckTokenRequest(
        @Email
        @NotNull
        String email,
        @Size(min = 6, max = 12)
        String code
) {
}