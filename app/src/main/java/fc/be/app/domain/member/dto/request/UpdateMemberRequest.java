package fc.be.app.domain.member.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record UpdateMemberRequest(
        @NotBlank
        String nickname,
        @Pattern(regexp = "^https?://\\S+$")
        String profile
) {
}
