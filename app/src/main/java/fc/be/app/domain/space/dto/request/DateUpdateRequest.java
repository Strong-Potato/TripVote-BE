package fc.be.app.domain.space.dto.request;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

import lombok.Builder;

@Builder
public record DateUpdateRequest(
        @NotNull(message = "시작날짜에 빈값이 올 수 없습니다.")
        LocalDate startDate,
        @NotNull(message = "종료날짜에 빈값이 올 수 없습니다.")
        LocalDate endDate) {

}
