package fc.be.openapi.tourapi.dto.form.common;

import java.util.List;

public record Items<T>(
        List<T> item
) {
}
