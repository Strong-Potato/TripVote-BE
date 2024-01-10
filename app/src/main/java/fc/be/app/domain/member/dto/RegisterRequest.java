package fc.be.app.domain.member.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record RegisterRequest(
        @NotBlank
        String token,
        @Email
        String email,
        @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*[!@#$%^&*()])(?=.*[0-9]).{8,16}$")
        String password,
        @NotBlank
        String nickname,
        String profile
) {
}
