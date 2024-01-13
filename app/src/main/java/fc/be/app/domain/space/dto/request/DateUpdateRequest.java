package fc.be.app.domain.space.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.time.LocalDate;

@Builder
public record DateUpdateRequest(
        @NotNull(message = "시작날짜에 빈값이 올 수 없습니다.")
        LocalDate startDate,
        @NotNull(message = "종료날짜에 빈값이 올 수 없습니다.")
        LocalDate endDate) {

}
