package fc.be.openapi.tourapi.dto.form.common;

public record Response<T>(
        Header header,
        Body<T> body
) {
}
