package fc.be.app.domain.notification.domain.event.vo;

public record SpaceEventInfo(
        Long spaceId,
        String spaceTitle,
        String oldTitle,
        String oldDates,
        String changeDate
) {
}
