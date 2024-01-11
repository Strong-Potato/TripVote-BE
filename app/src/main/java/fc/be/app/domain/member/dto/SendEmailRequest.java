package fc.be.app.domain.member.dto;

import jakarta.validation.constraints.Email;

public record SendEmailRequest(
        @Email
        String email
) {
}
