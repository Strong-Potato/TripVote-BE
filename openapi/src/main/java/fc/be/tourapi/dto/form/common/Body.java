package fc.be.tourapi.dto.form.common;

public record Body<T>(
        Items<T> items,
        int numOfRows,
        int pageNo,
        int totalCount
) {
}
