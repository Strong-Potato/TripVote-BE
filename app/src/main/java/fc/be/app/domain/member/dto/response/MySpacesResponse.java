package fc.be.app.domain.member.dto.response;

import fc.be.app.domain.space.dto.response.SpaceResponse;
import fc.be.app.domain.space.dto.response.SpacesResponse;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

public record MySpacesResponse(
        List<MySpace> spaces,
        Integer pageNumber,
        Integer pageSize,
        Integer totalPages,
        Long totalResult,
        boolean first,
        boolean last
) {
    public record MySpace(
            Long id,
            String title,
            LocalDate startDate,
            LocalDate endDate,
            Long dueDate
    ) {
        public static MySpace from(SpaceResponse spaceResponse) {
            return new MySpace(
                    spaceResponse.id(),
                    spaceResponse.title(),
                    spaceResponse.startDate(),
                    spaceResponse.endDate(),
                    spaceResponse.startDate() != null ?
                            ChronoUnit.DAYS.between(LocalDate.now(), spaceResponse.startDate()) : null
            );
        }
    }

    public static MySpacesResponse from(SpacesResponse spacesResponse) {
        return new MySpacesResponse(
                spacesResponse.spaces().stream().map(MySpace::from).toList(),
                spacesResponse.pageNumber(),
                spacesResponse.pageSize(),
                spacesResponse.totalPages(),
                spacesResponse.totalResult(),
                spacesResponse.first(),
                spacesResponse.last()
        );
    }
}
