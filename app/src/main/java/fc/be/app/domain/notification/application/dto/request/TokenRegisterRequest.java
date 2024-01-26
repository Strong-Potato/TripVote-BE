package fc.be.app.domain.notification.application.dto.request;

public record TokenRegisterRequest(
        String token,
        Long memberId
) {

}
