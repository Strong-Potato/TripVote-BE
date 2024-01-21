package fc.be.app.domain.notification.exception;

public enum NotificationErrorCode {


    NOTIFICATION_NOT_FOUND(404, "NOTIFICATION_NOT_FOUND", "알림이 존재하지 않습니다."),
    NOT_FOUND_TOKEN(404, "TOKEN_NOT_FOUND", "알림 전송을 위한 토큰값이 존재하지 않습니다."),
    JSON_CONVERTING_ERROR(404, "JSON_CONVERTING_ERROR", "알림 메시지 JSON 파싱 실패!"),
    GOOGLE_REQUEST_TOKEN_ERROR(500, "GOOGLE_REQUEST_TOKEN_ERROR", "구글에 토큰 요청할 때 발생한 에러"),
    FIREBASE_CONNECT_ERROR(500, "FIREBASE_CONNECT_ERROR", "파이어베이스 접속 시 발생한 에러"),
    ;

    private final Integer responseCode;
    private final String title;
    private final String detail;

    NotificationErrorCode(Integer responseCode, String title, String detail) {
        this.responseCode = responseCode;
        this.title = title;
        this.detail = detail;
    }

    public Integer getResponseCode() {
        return responseCode;
    }

    public String getTitle() {
        return title;
    }

    public String getDetail() {
        return detail;
    }
}
