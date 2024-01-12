package fc.be.app.domain.review.exception;

import lombok.Getter;

@Getter
public enum ReviewErrorCode {
    REVIEW_NOT_FOUND(400, "review not found", "해당 리뷰가 존재하지 않습니다."),
    ;
    private final Integer responseCode;
    private final String title;
    private final String detail;

    ReviewErrorCode(Integer responseCode, String title, String detail) {
        this.responseCode = responseCode;
        this.title = title;
        this.detail = detail;
    }

}