package fc.be.app.global.http;

import org.springframework.http.HttpStatus;

public record ApiResponse<T>(
        int status,
        String message,
        T data
) {
    private static final HttpStatus OK = HttpStatus.OK;
    private static final HttpStatus CREATED = HttpStatus.CREATED;
    private static final String DEFAULT_MESSAGE = "SUCCESS";

    public static ApiResponse<Void> ok() {
        return new ApiResponse<>(OK.value(), DEFAULT_MESSAGE, null);
    }

    public static <T> ApiResponse<T> ok(T data) {
        return new ApiResponse<>(OK.value(), DEFAULT_MESSAGE, data);
    }

    public static ApiResponse<Void> created() {
        return new ApiResponse<>(CREATED.value(), DEFAULT_MESSAGE, null);
    }

    public static <T> ApiResponse<T> created(T data) {
        return new ApiResponse<>(CREATED.value(), DEFAULT_MESSAGE, data);
    }
}
