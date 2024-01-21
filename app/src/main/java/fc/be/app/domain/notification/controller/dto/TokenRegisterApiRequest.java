package fc.be.app.domain.notification.controller.dto;

import jakarta.validation.constraints.NotNull;

public record TokenRegisterApiRequest(
        @NotNull String token
) {

}
