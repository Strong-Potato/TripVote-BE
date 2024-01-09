package fc.be.app.domain.space.entity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import fc.be.app.domain.space.exception.SpaceException;
import java.time.LocalDate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class SpaceTest {

    @DisplayName("Space 엔티티의 title, date 값이 변경되는것을 체크한다.")
    @Test
    void update() {
        // given
        Space space = Space.builder()
            .build();

        // when
        space.update("여행", LocalDate.of(2024, 1, 3), LocalDate.of(2024, 1, 8));

        // then
        assertThat(space.getTitle()).isEqualTo("여행");
        assertThat(space.getStartDate()).isEqualTo(LocalDate.of(2024, 1, 3));
        assertThat(space.getEndDate()).isEqualTo(LocalDate.of(2024, 1, 8));
    }

    @DisplayName("Space 엔티티의 startDate가 endDate보다 이후 일이면 예외를 반환한다.")
    @Test
    void updateThrowException_INVALID_START_DATE() {
        // given
        Space space = Space.builder()
            .build();

        // when then
        assertThatThrownBy(() -> space.update("여행", LocalDate.of(2024, 1, 3),
            LocalDate.of(2024, 1, 2)))
            .isInstanceOf(SpaceException.class)
            .hasMessageContaining("시작일자는 종료일자보다 늦거나 같아야 합니다.");
    }
}