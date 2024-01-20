package fc.be.app.domain.space.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;

import java.util.List;

@Builder
public record TitleUpdateRequest(
        @NotNull
        @Size(min = 1)
        List<String> cities
) {

}
