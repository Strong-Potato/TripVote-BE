package fc.be.app.domain.vote.service.dto.response.vo;

import fc.be.app.domain.member.entity.Member;

public record MemberProfile(
        Long id,
        String nickName,
        String profileImageUrl
) {

    public static MemberProfile of(Member member) {
        return new MemberProfile(
                member.getId(),
                member.getNickname(),
                member.getProfile()
        );
    }
}
