package fc.be.global.exception;


import lombok.Getter;

@Getter
public enum ExternalServiceErrorCode {
    MYSQL_CONNECTION_FAIL("INTERNAL_SERVER_ERROR", "잠시 서비스가 중단되었습니다.");

    private final String title;
    private final String detail;

    ExternalServiceErrorCode(String title, String detail) {
        this.title = title;
        this.detail = detail;
    }
}
