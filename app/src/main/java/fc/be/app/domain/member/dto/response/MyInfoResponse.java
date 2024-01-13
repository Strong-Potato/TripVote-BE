package fc.be.app.domain.member.dto.response;

import fc.be.app.domain.member.service.MemberQuery;

public record MyInfoResponse(
        String nickname,
        String profile
) {
    public static MyInfoResponse from(MemberQuery.MemberResponse memberResponse) {
        return new MyInfoResponse(memberResponse.nickname(), memberResponse.profile());
    }
}
