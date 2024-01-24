package fc.be.app.domain.space.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SpaceErrorCode {
    SPACE_NOT_FOUND(404, "SPACE_NOT_FOUND", "여행스페이스 정보가 존재하지 않습니다."),
    INVALID_START_DATE(400, "INVALID_START_DATE", "시작일자는 종료일자보다 늦거나 같아야 합니다."),
    JOURNEY_NOT_FOUND(404, "JOURNEY_NOT_FOUND", "여행스페이스에 대한 여정정보가 존재하지 않습니다."),
    NOT_JOINED_MEMBER(404, "NOT_JOINED_MEMBER", "여행스페이스에 속한 회원이 아닙니다."),
    SPACE_IS_READ_ONLY(400, "SPACE_IS_READ_ONLY", "여행스페이스가 읽기전용이기 때문에 수정할 수 없습니다."),
    SPACE_MAX_COUNT_OVER(400, "SPACE_MAX_COUNT_OVER", "여행스페이스 생성 최대 개수를 초과하셨습니다."),
    NO_SUCH_CITY(404, "NO_SUCH_CITY", "해당 도시가 없습니다."),
    SELECTED_PLACES_COUNT_OVER(400, "SELECTED_PLACES_COUNT_OVER", "여정당 등록할 수 있는 최대 개수를 초과하셨습니다.");

    private final Integer responseCode;
    private final String title;
    private final String detail;
}
