package fc.be.app.domain.space.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record TitleUpdateRequest(
    @NotBlank(message = "타이틀은 빈값을 넣을 수 없습니다.")
    String title
) {

}
