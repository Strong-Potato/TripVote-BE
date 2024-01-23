package fc.be.app.domain.member.controller.dto.response;

import fc.be.app.domain.member.service.MemberQuery;
import fc.be.app.domain.member.vo.AuthProvider;

public record MyInfoResponse(
        String nickname,
        String profile,
        AuthProvider provider
) {
    public static MyInfoResponse from(MemberQuery.MemberResponse memberResponse) {
        return new MyInfoResponse(memberResponse.nickname(), memberResponse.profile(), memberResponse.provider());
    }
}
