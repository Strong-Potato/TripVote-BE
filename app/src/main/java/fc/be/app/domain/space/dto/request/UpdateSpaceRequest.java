package fc.be.app.domain.space.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class UpdateSpaceRequest {
    @Getter
    @NoArgsConstructor
    public static class TitleUpdateRequest {
        @NotBlank(message = "Title cannot be blank")
        private String title;

        @Builder
        private TitleUpdateRequest(String title) {
            this.title = title;
        }
    }

    @Getter
    @NoArgsConstructor
    public static class DateUpdateRequest {
        @NotNull(message = "Start date cannot be null")
        private LocalDate startDate;

        @NotNull(message = "End date cannot be null")
        private LocalDate endDate;

        @Builder
        private DateUpdateRequest(LocalDate startDate, LocalDate endDate) {
            this.startDate = startDate;
            this.endDate = endDate;
        }
    }
}
