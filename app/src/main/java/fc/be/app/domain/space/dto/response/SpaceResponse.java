package fc.be.app.domain.space.dto.response;

import fc.be.app.domain.space.entity.JoinedMember;
import fc.be.app.domain.space.entity.Space;
import lombok.Builder;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
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
        Long dueDate,
        List<MemberInfo> members
) {
    public static SpaceResponse of(Space space) {

        List<MemberInfo> memberInfos = new ArrayList<>();

        List<JoinedMember> joinedMembers = space.getJoinedMembers()
                .stream()
                .filter(joinedMember -> !joinedMember.isLeftSpace())
                .toList();

        for (JoinedMember joinedMember : joinedMembers) {
            memberInfos.add(MemberInfo.builder()
                    .id(joinedMember.getMember().getId())
                    .nickname(joinedMember.getMember().getNickname())
                    .profile(joinedMember.getMember().getProfile())
                    .build());
        }

        return
                SpaceResponse.builder()
                        .id(space.getId())
                        .title(space.getSpaceTitle())
                        .startDate(space.getStartDate())
                        .endDate(space.getEndDate())
                        .city(space.getCityToString())
                        .thumbnail(space.getCityThumbnail())
                        .dueDate(space.getStartDate() != null ?
                                ChronoUnit.DAYS.between(LocalDate.now(), space.getStartDate()) : null)
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
