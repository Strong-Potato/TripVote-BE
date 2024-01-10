package fc.be.app.domain.space.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SpaceErrorCode {
    SPACE_NOT_FOUND(404, "SPACE_NOT_FOUND", "여행스페이스 정보가 존재하지 않습니다."),
    INVALID_START_DATE(400, "INVALID_START_DATE", "시작일자는 종료일자보다 늦거나 같아야 합니다.");

    private final Integer responseCode;
    private final String title;
    private final String detail;
}