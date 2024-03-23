package fc.be.app.domain.member.controller.dto.request;

import fc.be.app.domain.member.controller.validation.Password;

public record DeleteMemberRequest(
        @Password
        String password
) {
}
