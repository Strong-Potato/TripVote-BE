package fc.be.global.http;

public record ApiResponse<T>(
        int status,
        String message,
        T data
) {
    private static final int DEFAULT_STATUS = 200;
    private static final String DEFAULT_MESSAGE = "SUCCESS";

    public static <T> ApiResponse success() {
        return new ApiResponse(DEFAULT_STATUS, DEFAULT_MESSAGE, null);
    }

    public static <T> ApiResponse success(T data) {
        return new ApiResponse(DEFAULT_STATUS, DEFAULT_MESSAGE, data);
    }
}
