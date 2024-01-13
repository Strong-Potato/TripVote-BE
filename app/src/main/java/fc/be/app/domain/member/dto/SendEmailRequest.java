package fc.be.app.domain.member.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

public record SendEmailRequest(
        @Email
        @NotNull
        String email
) {
}
