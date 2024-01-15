package fc.be.openapi.tourapi.dto.response.form.common;

public record Response<T>(
        Header header,
        Body<T> body
) {
}
