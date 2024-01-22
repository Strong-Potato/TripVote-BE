package fc.be.app.domain.space.dto.response;

import fc.be.app.domain.space.entity.JoinedMember;
import fc.be.app.domain.space.entity.Space;
import lombok.Builder;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Builder
public record SpaceResponse(
        Long id,
        String title,
        LocalDate startDate,
        LocalDate endDate,
        String city,
        String thumbnail,
        List<MemberInfo> members
) {
    public static SpaceResponse of(Space space) {

        List<MemberInfo> memberInfos = new ArrayList<>();

        for (JoinedMember joinedMember : space.getJoinedMembers()) {
            memberInfos.add(MemberInfo.builder()
                    .id(joinedMember.getMember().getId())
                    .nickname(joinedMember.getMember().getNickname())
                    .profile(joinedMember.getMember().getProfile())
                    .build());
        }

        return
                SpaceResponse.builder()
                        .id(space.getId())
                        .title(space.getTitle())
                        .startDate(space.getStartDate())
                        .endDate(space.getEndDate())
                        .city(space.getCity() != null ? String.join(",", space.getCity()) : null)
                        .thumbnail(space.getCityThumbnail())
                        .members(memberInfos)
                        .build();
    }

    @Builder
    public record MemberInfo(
            Long id,
            String nickname,
            String profile
    ) {

    }
}
