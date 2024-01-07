package fc.be.app.domain.place.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PlaceErrorCode {
    PLACE_NOT_LOADED(400, "PLACE_NOT_LOADED", "장소 정보를 불러오지 못했습니다"),
    ;
    private final Integer responseCode;
    private final String title;
    private final String detail;
}