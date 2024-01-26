package fc.be.app.domain.notification.controller.dto;

public record SubScribeApiRequest(
        boolean isGlobal,
        Long spaceId
) {
}
