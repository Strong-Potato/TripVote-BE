package fc.be.app.domain.space.entity;

import fc.be.app.domain.space.exception.SpaceException;
import fc.be.app.domain.vote.entity.Vote;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.IntStream;

import static fc.be.app.domain.space.exception.SpaceErrorCode.INVALID_START_DATE;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Comment("여행스페이스")
public class Space {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Comment("여행스페이스 id")
    private Long id;

    @Comment("제목")
    private String title;

    @Comment("시작일")
    private LocalDate startDate;

    @Comment("종료일")
    private LocalDate endDate;

    @OneToMany(mappedBy = "space")
    private List<Journey> journeys;

    @OneToMany(mappedBy = "space")
    private List<Vote> voteSpaces;

    @OneToMany(mappedBy = "space", cascade = CascadeType.ALL)
    private List<JoinedMember> joinedMembers;

    @Builder
    private Space(String title, LocalDate startDate, LocalDate endDate) {
        this.title = title;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public static Space create() {
        return Space.builder()
                .build();
    }

    public void addJourney(Journey journey) {
        journeys.add(journey);
    }

    public void removeJourney(Journey journey) {
        journeys.remove(journey);
    }

    public void updateByTitle(String title) {
        if (title != null && !title.isEmpty()) {
            this.title = title;
        }
    }

    public void updateByDates(LocalDate startDate, LocalDate endDate) {
        validationDates(startDate, endDate);

        this.startDate = startDate;
        this.endDate = endDate;
    }

    public void updateByDatesAndUpdateJourney(LocalDate startDate, LocalDate endDate) {
        validationDates(startDate, endDate);

        int originalDays = daysBetween(this.startDate, this.endDate);
        int newDays = daysBetween(startDate, endDate);

        if (originalDays > newDays) {
            updateJourneysForDecreasedDays(startDate, endDate, originalDays - newDays);
        } else if (originalDays < newDays) {
            updateJourneysForIncreasedDays(startDate, endDate, newDays - originalDays);
        } else {
            updateJourneysForSameDays(startDate, endDate);
        }

        this.startDate = startDate;
        this.endDate = endDate;
    }

    private void validationDates(LocalDate startDate, LocalDate endDate) {
        if (startDate != null && endDate != null && startDate.isAfter(endDate)) {
            throw new SpaceException(INVALID_START_DATE);
        }
    }

    private void updateJourneysForDecreasedDays(LocalDate startDate, LocalDate endDate, int daysToRemove) {
        for (int i = 0; i < daysToRemove; i++) {
            journeys.remove(journeys.size() - 1);
        }
        updateJourneysCommon(startDate, endDate);
    }

    private void updateJourneysForIncreasedDays(LocalDate startDate, LocalDate endDate, int daysToAdd) {
        for (int i = 0; i < daysToAdd; i++) {
            Journey newJourney = Journey.builder()
                    .date(startDate.plusDays(i)) // 날짜를 올바르게 설정
                    .space(this)
                    .build();
            journeys.add(newJourney);
        }

        updateJourneysCommon(startDate, endDate);
    }

    private void updateJourneysForSameDays(LocalDate startDate, LocalDate endDate) {
        updateJourneysCommon(startDate, endDate);
    }

    private void updateJourneysCommon(LocalDate startDate, LocalDate endDate) {
        IntStream.range(0, daysBetween(startDate, endDate) + 1)
                .forEach(index -> journeys.get(index).updateDate(startDate.plusDays(index)));
    }

    private static int daysBetween(LocalDate startDate, LocalDate endDate) {
        return (int) Math.abs(startDate.toEpochDay() - endDate.toEpochDay());
    }
}
