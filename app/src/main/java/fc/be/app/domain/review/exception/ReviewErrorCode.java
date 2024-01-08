package fc.be.app.domain.review.exception;

import lombok.Getter;

@Getter
public enum ReviewErrorCode {
    REVIEW_NOT_FOUND(301, "review not found", "해당 리뷰가 존재하지 않습니다."),
    CONTENT_TYPE_NOT_MATCH(302, "content type not match", "해당하는 콘텐츠 타입이 없습니다.")
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