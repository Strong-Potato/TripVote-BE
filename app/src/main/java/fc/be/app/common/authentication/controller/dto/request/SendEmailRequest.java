package fc.be.app.common.authentication.controller.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

public record SendEmailRequest(
        @Email
        @NotNull
        String email
) {
}
