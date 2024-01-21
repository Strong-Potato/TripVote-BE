package fc.be.app.domain.notification.domain.event.vo;

public record MemberEventInfo(
        Long memberId,
        String memberNickname,
        String memberImageUrl
) {
}
