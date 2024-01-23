package fc.be.app.domain.member.controller.dto.request;

import jakarta.validation.constraints.Pattern;

public record DeleteMemberRequest(
        @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*[!@#$%^&*()])(?=.*[0-9]).{8,16}$")
        String password
) {
}
