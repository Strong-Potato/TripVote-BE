package fc.be.app.domain.space.entity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.groups.Tuple.*;

import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class JourneyTest {

    @DisplayName("여정 리스트를 생성한다.")
    @Test
    void createJourney() {
        // given
        Space space = Space.builder()
            .title("부산여행")
            .startDate(LocalDate.of(2024, 1, 1))
            .endDate(LocalDate.of(2024, 1, 3))
            .build();

        // when
        List<Journey> journeys = Journey.createJourneys(space.getStartDate(), space.getEndDate(),
            space);

        // then
        assertThat(journeys).hasSize(3)
            .extracting("date", "space")
            .contains(
                tuple(LocalDate.of(2024, 1, 1), space),
                tuple(LocalDate.of(2024, 1, 2), space),
                tuple(LocalDate.of(2024, 1, 3), space)
            );
    }

}